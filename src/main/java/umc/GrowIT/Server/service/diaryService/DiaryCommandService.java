package umc.GrowIT.Server.service.diaryService;

import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiaryCommandService {

    DiaryResponseDTO.ModifyDiaryResultDTO modifyDiary(DiaryRequestDTO.ModifyDiaryDTO request, Long diaryId, Long userId);

    DiaryResponseDTO.CreateDiaryResultDTO createDiary(DiaryRequestDTO.CreateDiaryDTO request, Long userId);

    DiaryResponseDTO.DeleteDiaryResultDTO deleteDiary(Long diaryId, Long userId);

    DiaryResponseDTO.VoiceChatResultDTO chatByVoice(DiaryRequestDTO.VoiceChatDTO request, Long userId);

    DiaryResponseDTO.SummaryResultDTO createDiaryByVoice(DiaryRequestDTO.SummaryDTO request, Long userId);

    DiaryResponseDTO.AnalyzedDiaryResponseDTO analyze(Long diaryId);
}
