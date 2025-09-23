package umc.GrowIT.Server.web.dto.FlaskDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FlaskResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "Flask 응답")
    public static class EmotionAnalysisResponseDTO {
        @Schema(description = "분석된 감정 키워드 목록")
        private List<SimilarityResultDTO> analyzedEmotions;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimilarityResultDTO {
        private String inputEmotion;
        private String similarEmotion;
        private Double similarityScore;
    }
}
