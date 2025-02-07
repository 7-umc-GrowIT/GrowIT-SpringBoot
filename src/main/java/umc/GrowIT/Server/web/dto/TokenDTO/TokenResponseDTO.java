package umc.GrowIT.Server.web.dto.TokenDTO;

import lombok.*;

public class TokenResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDTO {
        private String accessToken;
        private String refreshToken;
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
