package umc.GrowIT.Server.service.GroService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public GroResponseDTO createGro(Long userId, String nickname, String backgroundItem) {
        // 닉네임 체크
        checkNickname(nickname);

        // 유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 배경 아이템 조회
        Item item = itemRepository.findByName(backgroundItem)
                .orElseThrow(() -> new ItemHandler(ErrorStatus.ITEM_NOT_FOUND));

        // Gro 생성 및 저장
        Gro gro = GroConverter.toGro(user, nickname);
        Gro savedGro = groRepository.save(gro);

        // UserItem 생성 및 저장 (Converter 사용)
        UserItem userItem = UserItemConverter.toUserItem(user, item);
        userItemRepository.save(userItem);

        return GroConverter.toGroResponseDTO(savedGro);
    }

    @Transactional(readOnly = true)
    public void checkNickname(String nickname){

//        if (nickname.length() < 2 || nickname.length() > 10) {
//
//            throw new GroHandler(ErrorStatus.GRO_NICKNAME_LENGTH_INVALID);
//        }

        Optional<Gro> gro = groRepository.findByName(nickname);

        if (gro.isPresent()){
            throw new GroHandler(ErrorStatus.GRO_NICKNAME_ALREADY_EXISTS);
        }

    }
}
