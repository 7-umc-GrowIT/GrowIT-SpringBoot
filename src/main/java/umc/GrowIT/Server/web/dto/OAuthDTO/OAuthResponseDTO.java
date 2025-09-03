package umc.GrowIT.Server.web.dto.OAuthDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;

public class OAuthResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuthLoginDTO {

        private Boolean signupRequired;

        private OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfo;

        private AuthResponseDTO.LoginResponseDTO loginResponseDTO;
    }
}
