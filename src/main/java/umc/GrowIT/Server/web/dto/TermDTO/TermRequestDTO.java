package umc.GrowIT.Server.web.dto.TermDTO;

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
    public static class UserTermDTO {

        @NotEmpty
        private Long termId;

        @NotNull
        private Boolean agreed;
    }
}
