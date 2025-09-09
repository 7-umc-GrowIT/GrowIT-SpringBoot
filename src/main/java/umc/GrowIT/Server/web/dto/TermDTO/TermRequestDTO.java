package umc.GrowIT.Server.web.dto.TermDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class TermRequestDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "동의할 약관 항목 Request")
    public static class UserTermDTO {

        @Schema(description = "약관 ID", example = "7")
        @NotEmpty
        private Long termId;

        @Schema(description = "동의 여부", example = "true")
        @NotNull
        private Boolean agreed;
    }
}
