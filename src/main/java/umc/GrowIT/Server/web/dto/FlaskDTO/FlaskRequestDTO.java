package umc.GrowIT.Server.web.dto.FlaskDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FlaskRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "Flask 요청")
    public static class EmotionAnalysisRequestDTO {
        @Schema(title = "감정 키워드 목록")
        private List<String> emotions;
    }
}
