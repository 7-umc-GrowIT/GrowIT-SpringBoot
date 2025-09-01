package umc.GrowIT.Server.web.dto.TokenDTO;

import lombok.*;
import umc.GrowIT.Server.domain.enums.LoginMethod;

public class TokenResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class TokenDTO {
        private String accessToken;
        private String refreshToken;
        private LoginMethod loginMethod;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class AccessTokenDTO {
        private String accessToken;
    }
}
