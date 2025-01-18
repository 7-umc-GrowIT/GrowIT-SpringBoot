package umc.GrowIT.Server.service.diaryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.DiaryConverter;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.repository.ItemRepository.ItemRepository;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.web.dto.DiaryDTO.DiaryResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryQueryServiceImpl implements DiaryQueryService{

    private final DiaryRepository diaryRepository;
    @Override
    public DiaryResponseDTO.DiaryDateListDTO getDiaryDate(Integer year, Integer month, Long userId) {
        // Diary 리스트를 year와 month를 기준으로 필터링
        List<Diary> diaryList = diaryRepository.findByUserIdAndYearAndMonth(userId, year, month);


        return DiaryConverter.toDiaryDateListDTO(diaryList);
    }
}
