package umc.GrowIT.Server.web.dto.KeywordDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class KeywordResponseDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordDTO {
        @Schema(description = "감정 키워드 id", example = "1")
        private Long id;
        @Schema(description = "감정 키워드 내용", example = "행복한")
        private String keyword;
    }
}
