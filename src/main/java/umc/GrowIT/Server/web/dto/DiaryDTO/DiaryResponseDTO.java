package umc.GrowIT.Server.web.dto.DiaryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class DiaryResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryDateDTO{
        Long diaryId;
        LocalDateTime date;    //일기 최종 수정 날짜
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
        String title;
        String content;
        LocalDateTime date;    //일기 최종 수정 날짜
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiaryListDTO{
        List<DiaryResponseDTO.DiaryDTO> diaryList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResultDTO{
        Long diaryId;
        String title;
        String content;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyResultDTO{
        Long diaryId;
        String title;
        String content;
        LocalDateTime updatedAt;
    }
}
