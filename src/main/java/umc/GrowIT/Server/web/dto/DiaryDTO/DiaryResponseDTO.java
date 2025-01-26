package umc.GrowIT.Server.web.dto.DiaryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public static class DiaryListDTO{
        List<DiaryResponseDTO.DiaryDTO> diaryList;
        Integer listSize;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResultDTO{
        Long diaryId;
        String content;
        LocalDate date;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyResultDTO{
        Long diaryId;
        String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteResultDTO{
        String message;
    }
}
