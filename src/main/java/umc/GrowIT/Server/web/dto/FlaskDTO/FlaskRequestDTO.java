package umc.GrowIT.Server.web.dto.FlaskDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FlaskRequestDTO {

    // Flask 요청 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmotionAnalysisRequestDTO {
        private List<String> emotions;
    }
}
