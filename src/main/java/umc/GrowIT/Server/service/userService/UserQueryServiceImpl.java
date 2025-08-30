package umc.GrowIT.Server.service.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.UserConverter;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import static umc.GrowIT.Server.domain.enums.UserStatus.INACTIVE;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public boolean isUserInactive(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return user.getStatus() == INACTIVE;
    }

    @Override
    public UserResponseDTO.MyPageDTO getMyPage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        return UserConverter.toMyPageDTO(user);
    }
}
