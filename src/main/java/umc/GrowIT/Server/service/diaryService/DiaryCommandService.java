package umc.GrowIT.Server.service.diaryService;

import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiaryCommandService {

    public DiaryResponseDTO.ModifyDiaryResultDTO modifyDiary(DiaryRequestDTO.ModifyDiaryDTO request, Long diaryId, Long userId);

    public DiaryResponseDTO.CreateDiaryResultDTO createDiary(DiaryRequestDTO.CreateDiaryDTO request, Long userId);

    public DiaryResponseDTO.DeleteDiaryResultDTO deleteDiary(Long diaryId, Long userId);

    public DiaryResponseDTO.VoiceChatResultDTO chatByVoice(DiaryRequestDTO.VoiceChatDTO request, Long userId);

    public DiaryResponseDTO.SummaryResultDTO createDiaryByVoice(DiaryRequestDTO.SummaryDTO request, Long userId);

    DiaryResponseDTO.AnalyzedDiaryResponseDTO analyze(Long diaryId);
}
