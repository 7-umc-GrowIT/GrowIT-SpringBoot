package umc.GrowIT.Server.service.diaryService;

import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiaryCommandService {

    DiaryResponseDTO.ModifyDiaryResultDTO modifyDiary(DiaryRequestDTO.ModifyDiaryDTO request, Long diaryId, Long userId);

    DiaryResponseDTO.SaveDiaryResultDTO saveDiaryByText(DiaryRequestDTO.SaveTextDiaryDTO request, Long userId);

    void deleteDiary(Long diaryId, Long userId);

    DiaryResponseDTO.VoiceChatResultDTO chatByVoice(DiaryRequestDTO.VoiceChatDTO request, Long userId);

    DiaryResponseDTO.SaveDiaryResultDTO saveDiaryByVoice(DiaryRequestDTO.SaveVoiceDiaryDTO request, Long userId);

    DiaryResponseDTO.AnalyzedDiaryResponseDTO analyze(Long diaryId);
}
