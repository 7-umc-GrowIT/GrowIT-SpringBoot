package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.Role;
import umc.GrowIT.Server.domain.enums.UserStatus;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

public class UserConverter {

    public static User toUser(UserRequestDTO.UserInfoDTO userInfoDTO) {
        return User.builder()
                .name(userInfoDTO.getName())
                .email(userInfoDTO.getEmail())
                .password(userInfoDTO.getPassword())
                .status(UserStatus.ACTIVE)
                .role(Role.USER)
                .currentCredit(0)
                .totalCredit(0)
                .build();
    }

    public static UserResponseDTO.DeleteUserResponseDTO toDeletedUser(User deleteUser) {
        return UserResponseDTO.DeleteUserResponseDTO.builder()
                .name(deleteUser.getName())
                .message("회원탈퇴가 완료되었어요")
                .build()
                ;
    }
}