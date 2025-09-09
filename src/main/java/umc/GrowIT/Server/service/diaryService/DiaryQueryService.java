package umc.GrowIT.Server.service.diaryService;

import umc.GrowIT.Server.domain.enums.CreditSource;
import umc.GrowIT.Server.domain.enums.DiaryType;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

public interface DiaryQueryService {

    boolean hasVoiceDiaries(Long userId);

    DiaryResponseDTO.DiaryDateListDTO getDiaryDate(Integer year, Integer month, Long userId);

    DiaryResponseDTO.DiaryListDTO getDiaryList(Integer year, Integer month, Long userId);

    DiaryResponseDTO.DiaryDTO getDiary(Long diaryId, Long userId);
}
