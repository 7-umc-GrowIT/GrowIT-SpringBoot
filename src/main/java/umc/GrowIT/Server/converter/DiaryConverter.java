package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class DiaryConverter {
    public static DiaryResponseDTO.DiaryDateDTO toDiaryDateDTO(Diary diary){
        return DiaryResponseDTO.DiaryDateDTO.builder()
                .diaryId(diary.getId())
                .date(diary.getDate())
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

    public static DiaryResponseDTO.DiaryDTO toDiaryDTO(Diary diary){
        //일기의 id, 내용, 생성일
        return DiaryResponseDTO.DiaryDTO.builder()
                .diaryId(diary.getId())
                .content(diary.getContent())
                .date(diary.getDate())
                .build();
    }

    public static DiaryResponseDTO.DiaryListDTO toDiaryListDTO(List<Diary> diaryList){

        List<DiaryResponseDTO.DiaryDTO> diaryDTOList = diaryList.stream()
                .map(DiaryConverter::toDiaryDTO).collect(Collectors.toList());

        return DiaryResponseDTO.DiaryListDTO.builder()
                .diaryList(diaryDTOList)
                .listSize(diaryDTOList.size())
                .build();
    }

    public static DiaryResponseDTO.ModifyResultDTO toModifyResultDTO(Diary diary){

        return DiaryResponseDTO.ModifyResultDTO.builder()
                .diaryId(diary.getId())
                .content(diary.getContent())
                .updatedAt(diary.getUpdatedAt())
                .build();
    }
}
