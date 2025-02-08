package umc.GrowIT.Server.service.KeywordService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final DiaryRepository diaryRepository;

    public List<String> getRecentDiaryKeywords(Long userId) {
        return diaryRepository.findLatestDiaryByUserId(userId)
                .map(diary -> diary.getDiaryKeywords().stream()
                        .map(diaryKeyword -> diaryKeyword.getKeyword().getName())
                        .limit(3)
                        .toList())
                .orElseGet(Collections::emptyList); // 일기가 없으면 빈 리스트 반환
    }

}

