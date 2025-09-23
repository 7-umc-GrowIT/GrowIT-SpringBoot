package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.enums.LoginMethod;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

import static umc.GrowIT.Server.converter.UserConverter.toLoginResponseDTO;
import static umc.GrowIT.Server.domain.enums.Provider.KAKAO;

public class OAuthConverter {

    public static OAuthApiResponseDTO.OAuthUserInfoDTO toOAuthUserInfoDTO(OAuthApiResponseDTO.KakaoUserInfoResponseDTO kakaoUserInfo) {
        return OAuthApiResponseDTO.OAuthUserInfoDTO.builder()
                .socialId(String.valueOf(kakaoUserInfo.getId()))
                .email(kakaoUserInfo.getKakao_account().getEmail())
                .name(kakaoUserInfo.getKakao_account().getProfile().getNickname())
                .provider("KAKAO")
                .build();
    }

    public static OAuthApiResponseDTO.OAuthUserInfoDTO toOAuthUserInfoDTO(String sub, String email) {
        return OAuthApiResponseDTO.OAuthUserInfoDTO.builder()
                .socialId(sub)
                .email(email)
                .provider("APPLE")
                .build();
    }

    public static OAuthResponseDTO.OAuthLoginDTO toOAuthLoginDTO(Boolean signupRequired, OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfo, AuthResponseDTO.LoginResponseDTO loginResponseDTO) {
        return OAuthResponseDTO.OAuthLoginDTO.builder()
                .signupRequired(signupRequired)
                .oAuthUserInfo(oAuthUserInfo)
                .loginResponseDTO(loginResponseDTO)
                .build();
    }
}
