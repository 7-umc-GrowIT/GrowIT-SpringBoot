package umc.GrowIT.Server.web.dto.DiaryDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class DiaryRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 작성 request")
    public static class CreateDiaryDTO {
        @Schema(description = "일기 작성 내용", example = "오늘은 미라클 모닝을 해서 아침 8시에 학교에 도착했다. 그런데 강사님께서 ~")
        @NotNull
        String content;
        @Schema(description = "일기 작성 날짜")
        @NotNull
        LocalDate date;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 수정 request")
    public static class ModifyDiaryDTO {
        @Schema(description = "일기 수정 내용", example = "오늘은 미라클 모닝을 해서 아침 8시에 학교에 도착했다. 그런데 강사님께서 ~")
        @NotNull
        String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "음성 대화 request")
    public static class VoiceChatDTO {
        @Schema(description = "사용자의 음성내용", example = "오늘 정말 힘든 하루였어.")
        @NotNull
        String chat;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 요약 request")
    public static class SummaryDTO {
        @Schema(description = "일기 작성 날짜")
        @NotNull
        LocalDate date;
    }
}
