package umc.GrowIT.Server.web.dto.AuthDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class AuthRequestDTO {

    // 이메일 인증 전송 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendAuthEmailRequestDTO {
        @NotBlank
        @Email
        private String email; // 인증번호 받을 이메일
    }

    // 인증 코드 확인 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerifyAuthCodeRequestDTO {
        private String authCode; // 인증 번호
    }
}
