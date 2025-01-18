package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DiaryConverter {
    public static DiaryResponseDTO.DiaryDateDTO toDiaryDateDTO(Diary diary){
        return DiaryResponseDTO.DiaryDateDTO.builder()
                .diaryId(diary.getId())
                .date(diary.getCreatedAt())
                .build();
    }

    public static DiaryResponseDTO.DiaryDateListDTO toDiaryDateListDTO(List<Diary> diaryList){

        List<DiaryResponseDTO.DiaryDateDTO> diaryDateDTOList = diaryList.stream()
                .map(DiaryConverter::toDiaryDateDTO).collect(Collectors.toList());

        return DiaryResponseDTO.DiaryDateListDTO.builder()
                .diaryDateList(diaryDateDTOList)
                .listSize(diaryDateDTOList.size())
                .build();
    }
}
