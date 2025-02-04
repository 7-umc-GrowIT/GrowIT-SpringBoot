package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthResponseDTO;

import static umc.GrowIT.Server.domain.enums.Provider.KAKAO;

public class OAuthAccountConverter {
    public static OAuthAccount toOAuthAccount(OAuthResponseDTO.KakaoUserInfoResponseDTO kakaoUserInfo, User user) {
        return OAuthAccount.builder()
                .providerId(kakaoUserInfo.getId())
                .provider(KAKAO)
                .email(kakaoUserInfo.getKakao_account().getEmail())
                .user(user)
                .build();
    }
}
