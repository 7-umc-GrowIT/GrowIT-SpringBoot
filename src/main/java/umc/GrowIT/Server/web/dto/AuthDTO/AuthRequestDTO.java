package umc.GrowIT.Server.web.dto.AuthDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AuthRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "인증 이메일 발송 Request")
    public static class SendAuthEmailRequestDTO {
        @Email
        @NotBlank
        @Schema(description = "인증번호 받을 이메일", example = "GrowIT2025@gmail.com")
        private String email;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "인증번호 검증 Request")
    public static class VerifyAuthCodeRequestDTO {
        @Email
        @NotBlank
        @Schema(description = "인증번호 받은 이메일", example = "GrowIT2025@gmail.com")
        private String email;

        @NotBlank
        @Schema(description = "인증번호", example = "T3hUWX1t")
        @Pattern(regexp = "^[A-Za-z\\d]{8}$", message = "인증번호는 문자 또는 숫자 8자리입니다.")
        private String authCode;
    }
}