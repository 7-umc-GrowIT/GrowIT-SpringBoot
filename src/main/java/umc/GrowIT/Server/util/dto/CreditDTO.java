package umc.GrowIT.Server.util.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreditDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditInfo {
        @Schema(description = "크레딧 지급 여부", example = "true")
        Boolean granted;

        @Schema(description = "지급된 크레딧 양 (지급되지 않은 경우 0)", example = "10")
        Integer amount;
    }
}
