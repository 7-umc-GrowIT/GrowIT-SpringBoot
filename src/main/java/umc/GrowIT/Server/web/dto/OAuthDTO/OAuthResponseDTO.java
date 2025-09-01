package umc.GrowIT.Server.web.dto.OAuthDTO;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

public class OAuthResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(NON_NULL)
    public static class OAuthLoginDTO {

        private Boolean signupRequired;

        private OAuthApiResponseDTO.OAuthUserInfoDTO oAuthUserInfo;

        private TokenResponseDTO.TokenDTO tokens;
    }
}
