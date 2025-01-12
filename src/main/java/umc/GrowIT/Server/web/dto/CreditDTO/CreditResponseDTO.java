package umc.GrowIT.Server.web.dto.CreditDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreditResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditDTO {
        @Schema(description = "현재 보유한 크레딧", example = "1200")
        private Integer credit;
    }



}
