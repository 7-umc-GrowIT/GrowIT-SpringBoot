package umc.GrowIT.Server.web.dto.DiaryDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public class DiaryRequestDTO {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "직접 일기 임시저장 request")
    public static class SaveTextDiaryDTO {

        @NotNull
        @Size(min = 100, message = "일기는 100자 이상으로 작성해야 합니다.")
        @Schema(description = "일기 작성 내용", example = "오늘은 미라클 모닝을 해서 아침 8시에 학교에 도착했다. 그런데 강사님께서 ~")
        private String content;

        @NotNull
        @Schema(description = "일기 작성 날짜")
        private LocalDate date;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 수정 request")
    public static class ModifyDiaryDTO {
        @NotNull
        @Size(min = 100, message = "일기는 100자 이상으로 작성해야 합니다.")
        @Schema(description = "일기 수정 내용", example = "오늘은 미라클 모닝을 해서 아침 8시에 학교에 도착했다. 그런데 강사님께서 ~")
        private String content;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "AI 음성 대화 request")
    public static class VoiceChatDTO {
        @NotNull
        @Schema(description = "사용자의 음성내용", example = "오늘 정말 힘든 하루였어.")
        private String chat;

        @NotNull
        @Schema(description = "할 말 추가 여부", example = "false")
        private Boolean hasAdditionalChat;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "음성 일기 임시저장 request")
    public static class SaveVoiceDiaryDTO {
        @NotNull
        @Schema(description = "일기 작성 날짜")
        private LocalDate date;
    }
}
