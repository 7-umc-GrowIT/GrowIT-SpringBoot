package umc.GrowIT.Server.web.dto.WithdrawalDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class WithdrawalResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithdrawalReasonDTO {

        @Schema(description = "탈퇴이유 ID", example = "1")
        private Long id;

        @Schema(description = "탈퇴이유 내용", example = "원하는 기능이 부족해요")
        private String reason;
    }
}
