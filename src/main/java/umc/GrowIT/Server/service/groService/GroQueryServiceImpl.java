package umc.GrowIT.Server.service.groService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.GroHandler;
import umc.GrowIT.Server.apiPayload.exception.ItemHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.GroConverter;
import umc.GrowIT.Server.domain.Gro;
import umc.GrowIT.Server.domain.Item;
import umc.GrowIT.Server.domain.UserItem;
import umc.GrowIT.Server.repository.GroRepository;
import umc.GrowIT.Server.repository.UserItemRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.util.S3Util;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroQueryServiceImpl implements GroQueryService {

    private final GroRepository groRepository;
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;
    private final S3Util s3Util;

    @Override
    @Transactional(readOnly = true)
    public GroResponseDTO.GroAndEquippedItemsDTO getGroAndEquippedItems(Long userId) {
        // 1. 사용자 조회
        userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 그로 조회
        Gro gro = groRepository.findByUserId(userId)
                .orElseThrow(() -> new GroHandler(ErrorStatus.GRO_NOT_FOUND));

        // 3. 착용 중인 아이템 조회
        List<UserItem> equippedUserItems = userItemRepository.findEquippedItemsByUserId(userId);
        if (equippedUserItems.isEmpty()) {
            throw new ItemHandler(ErrorStatus.EQUIPPED_USER_ITEM_NOT_FOUND);
        }

        // 4. 프리사인드 URL 생성
        String groUrl = createGroPreSignedUrl(gro.getLevel());
        Map<UserItem, String> itemUrls = createItemPreSignedUrl(equippedUserItems);

        // 5. converter 작업
        return GroConverter.toGroAndEquippedItemsDTO(gro, groUrl, itemUrls);
    }




    /*
        이 아래부터 헬퍼메소드
    */

    // 그로 캐릭터 이미지에 대한 Pre-signed URL 생성
    private String createGroPreSignedUrl(Integer groLevel) {
        String imageKey = switch (groLevel) {
            case 1 -> "gro/gro_head.png";
//            case 2 -> "gro/gro2_head.png";
            default -> throw new GroHandler(ErrorStatus.GRO_LEVEL_INVALID);
        };

        return s3Util.toGetPresignedUrl(imageKey, Duration.ofMinutes(15));
    }

    // 착용 중인 이미지에 대한 Pre-signed URL 생성
    private Map<UserItem, String> createItemPreSignedUrl(List<UserItem> equippedUserItems) {
        return equippedUserItems.stream()
                .collect(Collectors.toMap(
                        userItem -> userItem,
                        userItem -> s3Util.toGetPresignedUrl(userItem.getItem().getGroImageKey(), Duration.ofMinutes(15))
                ));
    }
}
