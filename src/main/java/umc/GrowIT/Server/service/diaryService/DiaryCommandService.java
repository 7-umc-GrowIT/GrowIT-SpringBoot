package umc.GrowIT.Server.service.diaryService;

import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiaryCommandService {
    public DiaryResponseDTO.ModifyResultDTO modifyDiary(DiaryRequestDTO.DiaryDTO request, Long diaryId, Long userId);

    public DiaryResponseDTO.CreateResultDTO createDiary(DiaryRequestDTO.DiaryDTO request, Long userId);
}
