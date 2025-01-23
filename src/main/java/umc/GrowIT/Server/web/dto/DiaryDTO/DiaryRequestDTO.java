package umc.GrowIT.Server.web.dto.DiaryDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class DiaryRequestDTO {
    @Getter
    @Setter
    public static class DiaryDTO {
        @NotNull
        String content; //작성 내용
        @NotNull
        LocalDate date; //작성 날짜
    }
}
