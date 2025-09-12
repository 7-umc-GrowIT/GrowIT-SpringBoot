package umc.GrowIT.Server.web.dto.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.enums.LoginMethod;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

public class AuthResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "로그아웃 response")
    public static class LogoutResponseDTO {
        @Schema(description = "로그아웃 메시지", example = "로그아웃이 완료되었습니다.")
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "로그인 response")
    public static class LoginResponseDTO {
        @Schema(description = "발급받은 토큰 목록")
        private TokenResponseDTO.TokenDTO tokens;
        @Schema(description = "로그인 방식", example = "LOCAL")
        private LoginMethod loginMethod;
    }
}
