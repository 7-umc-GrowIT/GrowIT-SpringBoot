package umc.GrowIT.Server.web.dto.DiaryDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DiaryRequestDTO {
    @Getter
    @Setter
    public static class DiaryDTO {
        @NotNull
        String content;
        @NotNull
        LocalDateTime date;
    }
}
