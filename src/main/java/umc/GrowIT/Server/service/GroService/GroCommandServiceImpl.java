package umc.GrowIT.Server.service.GroService;

import com.amazonaws.services.s3.AmazonS3;
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
import umc.GrowIT.Server.repository.GroRepository.GroRepository;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.repository.UserItemRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GroCommandServiceImpl implements GroCommandService{

    private final GroRepository groRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

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

    public void checkNickname(String nickname){

        Optional<Gro> gro = groRepository.findByName(nickname);

        if (gro.isPresent()){
            throw new GroHandler(ErrorStatus.GRO_NICKNAME_ALREADY_EXISTS);
        }
    }

    @Override
    @Transactional
    public void updateNickname(Long userId, String nickname) {
        // 사용자가 존재하지 않으면 에러 발생
        userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        //  그로가 존재하지 않으면 에러 발생
        Gro gro = groRepository.findByUserId(userId)
                .orElseThrow(() -> new GroHandler(ErrorStatus.GRO_NOT_FOUND));

        // 닉네임에 변경사항 없는 경우 바로 반환
        if (nickname.equals(gro.getName())) {
            return;
        }

        // 본인 제외 중복 검사
        checkNickname(nickname);

        // 수정된 닉네임 저장
        gro.setName(nickname);
    }
}
