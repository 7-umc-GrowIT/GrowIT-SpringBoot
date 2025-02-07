package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;

import static umc.GrowIT.Server.domain.enums.Provider.KAKAO;

public class OAuthAccountConverter {
    public static OAuthAccount toOAuthAccount(OAuthApiResponseDTO.OAuthUserInfoDTO oAuthuserInfoDTO, User newUser) {
        return OAuthAccount.builder()
                .provider(KAKAO)
                .providerId(oAuthuserInfoDTO.getId())
                .email(oAuthuserInfoDTO.getEmail())
                .user(newUser)
                .build();
    }
}
