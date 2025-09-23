package umc.GrowIT.Server.web.dto.TermDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TermResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "약관 조회 response")
    public static class TermDTO {

        @Schema(description = "약관 ID", example = "7")
        private Long termId;

        @Schema(description = "약관 제목", example = "그로우잇 이용약관")
        private String title;

        @Schema(description = "약관 내용", example = "제1조 (목적) 이 약관은 그로우잇...")
        private String content;

        @Schema(description = "약관 필수, 선택 여부", example = "MANDATORY")
        private String type;
    }
}
