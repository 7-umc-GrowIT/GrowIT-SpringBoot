package umc.GrowIT.Server.web.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDTO {

    // 이메일 인증 전송 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendAuthEmailRequestDTO {
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

    // 비밀번호 재설정 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResetPasswordRequestDTO {
        private String newPassword; // 새로운 비밀번호
        private String confirmPassword; // 비밀번호 확인
    }

    // 회원 탈퇴 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteUserRequestDTO {
        private String reason; // 탈퇴 사유
    }
}