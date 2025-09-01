package umc.GrowIT.Server.web.dto.OAuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class OAuthApiResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoTokenResponseDTO {
        private String token_type;
        private String access_token;
        private String id_token;
        private Integer expires_in;
        private String refresh_token;
        private Integer refresh_token_expires_in;
        private String scope;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoUserInfoResponseDTO {
        private Long id;
        private KakaoDTO.KakaoAccount kakao_account;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OAuthUserInfoDTO {
        @NotNull
        @Schema(description = "소셜 고유 ID", example = "001938.4ff14433e3a...")
        private String socialId;
        @NotNull
        @Schema(description = "이메일", example = "growit@gmail.com")
        private String email;
        @Schema(description = "사용자 이름", example = "growit")
        private String name;
        @Schema(description = "소셜 제공자", example = "APPLE")
        private String provider;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppleTokenResponseDTO {
        private String token_type;
        private String access_token;
        private String id_token;
        private Integer expires_in;
        private String refresh_token;
    }
}
