package umc.GrowIT.Server.dto;

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

        private String title;

        private String content;

        private String type; //필수, 선택 여부
    }
}
