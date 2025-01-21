package umc.GrowIT.Server.service.diaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryRequestDTO;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiaryCommandServiceImpl implements DiaryCommandService{
    private final DiaryRepository diaryRepository;
    @Override
    public DiaryResponseDTO.ModifyResultDTO modifyDiary(DiaryRequestDTO.DiaryDTO request, Long diaryId, Long userId) {

        Optional<Diary> diary = diaryRepository.findByUserIdAndId(diaryId, userId);


       return null;
    }
}
