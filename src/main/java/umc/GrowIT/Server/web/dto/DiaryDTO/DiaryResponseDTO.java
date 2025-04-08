package umc.GrowIT.Server.web.dto.DiaryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.time.LocalDate;
import java.util.List;

public class DiaryResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryDateDTO{
        Long diaryId;
        LocalDate date;    //일기 최종 수정 날짜
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryDateListDTO{
        List<DiaryDateDTO> diaryDateList;  //일기 작성 날짜들의 리스트
        Integer listSize;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryDTO{
        Long diaryId;
        String content;
        LocalDate date;    //일기 최종 수정 날짜
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryListDTO {
        List<DiaryDTO> diaryList;
        Integer listSize;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateDiaryResultDTO {
        Long diaryId;
        String content;
        LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyDiaryResultDTO {
        Long diaryId;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteDiaryResultDTO {
        String message;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoiceChatResultDTO {
        String chat;    //AI의 답변내용
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SummaryResultDTO {
        Long diaryId;
        String content;
        LocalDate date;
    }

    // 챌린지 추천 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnalyzedDiaryResponseDTO {
        private List<KeywordResponseDTO.KeywordDTO> emotionKeywords; //감정키워드
        private List<ChallengeResponseDTO.ChallengeDTO> recommendedChallenges; //추천챌린지
    }
}