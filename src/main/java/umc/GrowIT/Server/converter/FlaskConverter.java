package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.web.dto.FlaskDTO.FlaskRequestDTO;

import java.util.List;

public class FlaskConverter {
    // 챌린지 추천
    public static FlaskRequestDTO.EmotionAnalysisRequestDTO toEmotionAnalysisRequestDTO(List<String> inputEmotions) {
        return FlaskRequestDTO.EmotionAnalysisRequestDTO.builder()
                .emotions(inputEmotions)  // 빌더로 emotions 필드를 설정
                .build();
    }
}
