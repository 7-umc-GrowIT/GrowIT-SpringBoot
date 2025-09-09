package umc.GrowIT.Server.web.dto.TokenDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class TokenResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "토큰 발급 response")
    public static class TokenDTO {
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJ...")
        private String accessToken;
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJ...")
        private String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "액세스 토큰 발급 response")
    public static class AccessTokenDTO {
        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJ...")
        private String accessToken;
    }
}
