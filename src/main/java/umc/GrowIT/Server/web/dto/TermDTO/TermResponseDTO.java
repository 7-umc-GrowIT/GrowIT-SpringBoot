package umc.GrowIT.Server.web.dto.TermDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TermResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TermDTO {

        private Long termId;

        private String title;

        private String content;

        private String type; //필수, 선택 여부
    }
}
