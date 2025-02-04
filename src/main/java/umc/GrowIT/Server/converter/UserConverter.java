package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import static umc.GrowIT.Server.domain.enums.Role.USER;
import static umc.GrowIT.Server.domain.enums.UserStatus.ACTIVE;

public class UserConverter {

    public static User toUser(UserRequestDTO.UserInfoDTO userInfoDTO) {
        return User.builder()
                .name(userInfoDTO.getName())
                .primaryEmail(userInfoDTO.getEmail())
                .password(userInfoDTO.getPassword())
                .currentCredit(0)
                .totalCredit(0)
                .role(USER)
                .status(ACTIVE)
                .build();
    }

    public static UserResponseDTO.DeleteUserResponseDTO toDeletedUser(User deleteUser) {
        return UserResponseDTO.DeleteUserResponseDTO.builder()
                .name(deleteUser.getName())
                .message("회원탈퇴가 완료되었어요")
                .build();
    }

    public static UserResponseDTO.KakaoLoginDTO toKakaoLoginDTO(Boolean signupRequired, UserResponseDTO.TokenDTO tokens) {
        return UserResponseDTO.KakaoLoginDTO.builder()
                .signupRequired(signupRequired)
                .tokens(tokens)
                .build();
    }
}