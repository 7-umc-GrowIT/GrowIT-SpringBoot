package umc.GrowIT.Server.service.diaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.DiaryHandler;
import umc.GrowIT.Server.converter.DiaryConverter;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.repository.DiaryRepository;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryQueryServiceImpl implements DiaryQueryService{

    private final DiaryRepository diaryRepository;

    @Override
    public DiaryResponseDTO.DiaryDateListDTO getDiaryDate(Integer year, Integer month, Long userId) {
        // Diary 리스트를 year와 month를 기준으로 필터링
        List<Diary> diaryList = diaryRepository.findByUserIdAndYearAndMonth(userId, year, month);

        //DiaryDateListDTO로 변환
        return DiaryConverter.toDiaryDateListDTO(diaryList);
    }

    @Override
    public DiaryResponseDTO.DiaryListDTO getDiaryList(Integer year, Integer month, Long userId) {
        // Diary 리스트를 year와 month를 기준으로 필터링
        List<Diary> diaryList = diaryRepository.findByUserIdAndYearAndMonth(userId, year, month);

        //DiaryListDTO로 변환
        return DiaryConverter.toDiaryListDTO(diaryList);
    }

    @Override
    @Transactional(readOnly = true)
    public DiaryResponseDTO.DiaryDTO getDiary(Long diaryId, Long userId){
        Optional<Diary> diary = diaryRepository.findByUserIdAndId(userId, diaryId);

        return diary.map(DiaryConverter::toDiaryDTO)
                .orElseThrow(() -> new DiaryHandler(ErrorStatus.DIARY_NOT_FOUND));
    }
}
