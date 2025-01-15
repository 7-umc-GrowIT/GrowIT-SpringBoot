package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.Role;
import umc.GrowIT.Server.domain.enums.UserStatus;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;

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
}