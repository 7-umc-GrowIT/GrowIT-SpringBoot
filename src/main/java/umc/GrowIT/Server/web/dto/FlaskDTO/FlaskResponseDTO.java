package umc.GrowIT.Server.web.dto.FlaskDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FlaskResponseDTO {

    // Flask 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmotionAnalysisResponseDTO {
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
