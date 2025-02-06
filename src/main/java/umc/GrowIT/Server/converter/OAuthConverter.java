package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;

public class OAuthConverter {

    public static OAuthApiResponseDTO.OAuthUserInfoDTO toOAuthUserInfoDTO(OAuthApiResponseDTO.KakaoUserInfoResponseDTO kakaoUserInfo) {
        return OAuthApiResponseDTO.OAuthUserInfoDTO.builder()
                .id(kakaoUserInfo.getId())
                .email(kakaoUserInfo.getKakao_account().getEmail())
                .name(kakaoUserInfo.getKakao_account().getProfile().getNickname())
                .build();
    }
}
