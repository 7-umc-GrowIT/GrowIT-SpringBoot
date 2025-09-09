package umc.GrowIT.Server.web.dto.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.enums.LoginMethod;
import umc.GrowIT.Server.web.dto.TokenDTO.TokenResponseDTO;

import java.time.LocalDateTime;

public class AuthResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "인증 메일 전송 response")
    public static class SendAuthEmailResponseDTO {
        @Schema(description = "인증번호 받을 이메일", example = "GrowIT2025@gmail.com")
        private String email;
        @Schema(description = "인증 발송 성공 메시지", example = "이메일로 인증번호가 전송되었습니다.")
        private String message;
        @Schema(description = "인증번호", example = "T3hUWX1t")
        private String code;
        @Schema(description = "인증번호 만료기한", example = "2025-09-09T05:32:10.772Z")
        private LocalDateTime expiration;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "인증 코드 확인 response")
    public static class VerifyAuthCodeResponseDTO {
        @Schema(description = "인증 메시지", example = "인증이 완료되었습니다.")
        private String message;
    }

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
