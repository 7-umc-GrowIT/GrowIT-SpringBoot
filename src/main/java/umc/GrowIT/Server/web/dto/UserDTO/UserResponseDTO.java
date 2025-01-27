package umc.GrowIT.Server.web.dto.UserDTO;

import lombok.*;

import java.util.List;

public class UserResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDTO {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class AccessTokenDTO {
        private String accessToken;
    }

    // 회원 탈퇴 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteUserResponseDTO {
        // TODO 디테일하게 결정 필요
        private String name;
        private String message; // ex) 회원 탈퇴가 완료되었습니다
    }
}
