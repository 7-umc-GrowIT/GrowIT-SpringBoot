package umc.GrowIT.Server.web.dto.UserDTO;

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
}
