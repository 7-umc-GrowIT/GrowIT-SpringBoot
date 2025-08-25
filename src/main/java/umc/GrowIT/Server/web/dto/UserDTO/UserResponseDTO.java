package umc.GrowIT.Server.web.dto.UserDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserResponseDTO {

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
