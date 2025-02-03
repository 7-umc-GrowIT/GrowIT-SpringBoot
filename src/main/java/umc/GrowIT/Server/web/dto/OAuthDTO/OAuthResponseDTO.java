package umc.GrowIT.Server.web.dto.OAuthDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

import java.time.LocalDateTime;

public class OAuthResponseDTO {

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
        private LocalDateTime connected_at;
        private KakaoDTO.KakaoAccount kakao_account;
    }
}
