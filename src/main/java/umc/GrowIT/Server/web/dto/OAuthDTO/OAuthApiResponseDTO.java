package umc.GrowIT.Server.web.dto.OAuthDTO;

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
        private String socialId;
        @NotNull
        private String email;
        private String name;
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
