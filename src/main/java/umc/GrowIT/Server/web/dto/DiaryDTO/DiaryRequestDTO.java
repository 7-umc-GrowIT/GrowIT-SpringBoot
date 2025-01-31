package umc.GrowIT.Server.web.dto.DiaryDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class DiaryRequestDTO {
    @Getter
    @Setter
    public static class CreateDiaryDTO {
        @NotNull
        String content; //작성 내용
        @NotNull
        LocalDate date; //작성 날짜
    }

    @Getter
    @Setter
    public static class ModifyDiaryDTO {
        @NotNull
        String content; //수정 내용
    }
}
