package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.OAuthAccount;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.Provider;
import umc.GrowIT.Server.web.dto.OAuthDTO.OAuthApiResponseDTO;

public class OAuthAccountConverter {
    public static OAuthAccount toOAuthAccount(OAuthApiResponseDTO.OAuthUserInfoDTO oAuthuserInfoDTO, User newUser) {
        return OAuthAccount.builder()
                .provider(Provider.from(oAuthuserInfoDTO.getProvider()))
                .socialId(oAuthuserInfoDTO.getSocialId())
                .email(oAuthuserInfoDTO.getEmail())
                .user(newUser)
                .build();
    }
}
