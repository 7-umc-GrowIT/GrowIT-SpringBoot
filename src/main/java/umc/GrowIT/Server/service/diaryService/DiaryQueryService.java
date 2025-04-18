package umc.GrowIT.Server.service.diaryService;

import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiaryQueryService {
    public DiaryResponseDTO.DiaryDateListDTO getDiaryDate(Integer year, Integer month, Long userId);

    public DiaryResponseDTO.DiaryListDTO getDiaryList(Integer year, Integer month, Long userId);

    public DiaryResponseDTO.DiaryDTO getDiary(Long diaryId, Long userId);
}
