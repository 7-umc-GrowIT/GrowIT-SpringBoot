package umc.GrowIT.Server.web.dto.DiaryDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.time.LocalDate;
import java.util.List;

public class DiaryResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "작성된 일기 조회 response")
    public static class DiaryDateDTO{
        @Schema(description = "일기 id", example = "1")
        Long diaryId;
        @Schema(description = "일기 최종 수정 날짜")
        LocalDate date;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 작성 날짜 조회 response")
    public static class DiaryDateListDTO{
        @Schema(description = "일기 작성 날짜들의 리스트")
        List<DiaryDateDTO> diaryDateList;
        @Schema(description = "리스트 사이즈", example = "1")
        Integer listSize;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "작성된 일기 내용 조회 response")
    public static class DiaryDTO{
        @Schema(description = "일기 id", example = "1")
        Long diaryId;
        @Schema(description = "일기 작성 내용", example = "오늘은 미라클 모닝을 해서 아침 8시에 학교에 도착했다. 그런데 강사님께서 ~")
        String content;
        @Schema(description = "일기 최종 수정 날짜")
        LocalDate date;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 모아보기 response")
    public static class DiaryListDTO {
        @Schema(description = "일기 리스트")
        List<DiaryDTO> diaryList;
        @Schema(description = "리스트 사이즈", example = "1")
        Integer listSize;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 임시 저장 response")
    public static class SaveDiaryResultDTO {
        @Schema(description = "일기 id", example = "1")
        Long diaryId;
        @Schema(description = "일기 작성 내용", example = "오늘은 미라클 모닝을 해서 아침 8시에 학교에 도착했다. 그런데 강사님께서 ~")
        String content;
        @Schema(description = "일기 작성 날짜")
        LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "일기 수정 response")
    public static class ModifyDiaryResultDTO {
        @Schema(description = "일기 id", example = "1")
        Long diaryId;
        @Schema(description = "일기 수정 내용", example = "오늘은 미라클 모닝을 해서 아침 8시에 학교에 도착했다. 그런데 강사님께서 ~")
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "음성 대화 response")
    public static class VoiceChatResultDTO {
        @Schema(description = "AI의 답변 내용", example = "힘든 하루셨군요. 어떤 일이 있으셨나요?")
        String chat;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 추천 response")
    public static class AnalyzedDiaryResponseDTO {
        @Schema(description = "감정키워드")
        private List<KeywordResponseDTO.KeywordDTO> emotionKeywords;
        @Schema(description = "추천챌린지")
        private List<ChallengeResponseDTO.ChallengeDTO> recommendedChallenges;
    }
}