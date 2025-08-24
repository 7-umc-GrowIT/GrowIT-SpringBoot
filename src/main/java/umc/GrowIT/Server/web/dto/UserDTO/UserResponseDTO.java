package umc.GrowIT.Server.web.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserResponseDTO {

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

    // 마이페이지 조회 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyPageDTO {
        @Schema(description = "유저 ID", example="1")
        private Long userId;
        @Schema(description = "그로의 닉네임", example = "그로우")
        private String name;
    }
}
