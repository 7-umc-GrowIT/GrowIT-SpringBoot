package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;
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

    public static User toUser(OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfoDTO) {
        return User.builder()
                .name(oAuthUserInfoDTO.getName())
                .primaryEmail(oAuthUserInfoDTO.getEmail())
                .password(null)
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

    public static OAuthResponseDTO.KakaoLoginDTO toKakaoLoginDTO(Boolean signupRequired, OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfo, TokenResponseDTO.TokenDTO tokens) {
        return OAuthResponseDTO.KakaoLoginDTO.builder()
                .signupRequired(signupRequired)
                .oAuthUserInfo(oAuthUserInfo)
                .tokens(tokens)
                .build();
    }
}