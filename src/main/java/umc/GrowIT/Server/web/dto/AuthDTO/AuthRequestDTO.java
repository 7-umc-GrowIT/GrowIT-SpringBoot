package umc.GrowIT.Server.web.dto.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AuthRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "이메일 인증 전송 요청 Request")
    public static class SendAuthEmailRequestDTO {
        @Schema(description = "인증번호 받을 이메일", example = "GrowIT2025@gmail.com")
        @NotBlank
        @Email
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "인증 번호 확인 Request")
    public static class VerifyAuthCodeRequestDTO {
        @Schema(description = "인증번호 받을 이메일", example = "GrowIT2025@gmail.com")
        @NotBlank
        @Email
        private String email;

        @Schema(description = "인증번호", example = "T3hUWX1t")
        @NotBlank
        @Size(min = 8, max = 8, message = "인증번호는 문자+숫자 8자리입니다.")
        private String authCode;
    }
}