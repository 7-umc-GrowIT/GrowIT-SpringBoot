package umc.GrowIT.Server.service.KeywordService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.repository.KeywordRepository;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.domain.Keyword;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final DiaryRepository diaryRepository;
    private final KeywordRepository keywordRepository;

    public List<String> getTodayDiaryKeywords(Long userId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX);
        return diaryRepository.findTodayDiaryByUserId(userId, startOfDay, endOfDay)
                .map(diary -> diary.getDiaryKeywords().stream()
                        .map(diaryKeyword -> diaryKeyword.getKeyword().getName())
                        .limit(3)
                        .toList())
                .orElseGet(Collections::emptyList); // 일기가 없으면 빈 리스트 반환

    }
}

