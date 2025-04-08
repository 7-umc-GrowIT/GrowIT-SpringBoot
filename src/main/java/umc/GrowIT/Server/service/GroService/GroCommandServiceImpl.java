package umc.GrowIT.Server.service.GroService;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.GroHandler;
import umc.GrowIT.Server.apiPayload.exception.ItemHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.GroConverter;
import umc.GrowIT.Server.converter.UserItemConverter;
import umc.GrowIT.Server.domain.Gro;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.repository.GroRepository.GroRepository;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.repository.UserItemRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroCommandServiceImpl implements GroCommandService{

    private final AmazonS3 amazonS3;
    private final GroRepository groRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    @Transactional
    public GroResponseDTO.CreateResponseDTO createGro(Long userId, String nickname, String backgroundItem) {

        //사용자의 그로가 이미 존재하는 경우
        if(groRepository.existsByUserId(userId))
            throw new GroHandler(ErrorStatus.GRO_ALREADY_EXISTS);

        // 닉네임 체크
        checkNickname(nickname);

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 배경 아이템 조회
        Item item = itemRepository.findByName(backgroundItem)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.ITEM_NOT_FOUND));

        //기본 핑크색 PLANT 조회
        Item basicPlantItem = itemRepository.findByName("핑크 화분")
                .orElseThrow(() -> new ItemHandler(ErrorStatus.ITEM_NOT_FOUND));



        // Gro 생성 및 저장
        Gro gro = GroConverter.toGro(user, nickname);
        Gro savedGro = groRepository.save(gro);

        // UserItem 생성 및 저장 (Converter 사용)
        UserItem userItem = UserItemConverter.toUserItem(user, item);
        userItemRepository.save(userItem);

        //PLANT 저장
        userItem = UserItemConverter.toUserItem(user, basicPlantItem);
        userItemRepository.save(userItem);

        return GroConverter.toGroResponseDTO(savedGro);
    }


    @Transactional(readOnly = true)
    public void checkNickname(String nickname){

        Optional<Gro> gro = groRepository.findByName(nickname);

        if (gro.isPresent()){
            throw new GroHandler(ErrorStatus.GRO_NICKNAME_ALREADY_EXISTS);
        }
    }


    @Override
    public GroResponseDTO.GroAndEquippedItemsDTO getGroAndEquippedItems(Long userId) {
        // 1. userId 조회하고 없으면 오류
        userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));


        // 2. userId를 통해서 그로 조회하고 없으면 오류
        Gro gro = groRepository.findByUserId(userId)
                .orElseThrow(() -> new GroHandler(ErrorStatus.GRO_NOT_FOUND));


        // 3. userId를 통해서 사용자 아이템 조회하고 없으면 오류
        List<UserItem> userItems = userItemRepository.findAllWithItemsByUserId(userId);
        if (userItems.isEmpty()) {
            throw new ItemHandler(ErrorStatus.USER_ITEM_NOT_FOUND);
        }


        // 4. 사용자 아이템 중 착용한 것에 대해 필터링하고 없으면 오류
        List<UserItem> equippedUserItems = userItems.stream()
                .filter(userItem -> ItemStatus.EQUIPPED.equals(userItem.getStatus()))
                .collect(Collectors.toList());
        if (equippedUserItems.isEmpty()) {
            throw new ItemHandler(ErrorStatus.EQUIPPED_USER_ITEM_NOT_FOUND);
        }


        // 5. 착용한 아이템을 이용하여 Item 접근
        List<Item> equippedItems = equippedUserItems.stream()
                .map(userItem -> userItem.getItem())
                .collect(Collectors.toList());


        // 6. 프리사인드 URL 생성
        // 레벨에 맞는 이미지 선택하여 프리사인드 URL 생성
        String groUrl = createGroPreSignedUrl(gro.getLevel());

        // Item의 image_key값 얻어서 프리사인드 URL 생성
        Map<Item, String> itemUrls = createItemPreSignedUrl(equippedItems);


        // 7. converter 작업
        return GroConverter.toGroAndEquippedItemsDTO(gro, groUrl, itemUrls);
    }

    // 레벨에 맞는 imageKey로 프리사인드 URL 생성
    private String createGroPreSignedUrl(Integer groLevel) {
        // 레벨에 따른 그로 이미지가 제작되지 않은 관계로 일단 레벨 1에 대해서만 설정
        String imageKey = switch (groLevel) {
            case 1 -> "gro/gro_head.png";
//            case 2 -> "Gro/그로_2.png";
            default -> throw new GroHandler(ErrorStatus.GRO_LEVEL_INVALID);
        };

        // 프리사인드 URL 생성 (15분 유효 기간)
        Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15));

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, imageKey)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    // imageKey를 통해 S3 이미지 접근하여 프리사인드 URL 생성
    private Map<Item, String> createItemPreSignedUrl(List<Item> equippedItems) {
        return equippedItems.stream()
                .collect(Collectors.toMap(
                        item -> item, // key
                        item -> { // value
                            // 프리사인드 URL 생성 (15분 유효 기간)
                            Date expiration = new Date(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15));

                            GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, item.getGroImageKey())
                                    .withMethod(HttpMethod.GET)
                                    .withExpiration(expiration);

                            return amazonS3.generatePresignedUrl(generatePresignedUrlRequest).toString();
                        }
                ));
    }
}
