package umc.GrowIT.Server.web.dto.KeywordDTO;

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
        private Long id; // id
        private String keyword; // 감정
    }
}
