package umc.GrowIT.Server.service.KeywordService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.repository.KeywordRepository;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.domain.Keyword;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final DiaryRepository diaryRepository;
    private final KeywordRepository keywordRepository;

    public List<String> getRecentDiaryKeywords(Long userId) {
        // 최신 일기의 키워드 2개 가져오기
        List<String> diaryKeywordNames = diaryRepository.findLatestDiaryByUserId(userId)
                .map(diary -> diary.getDiaryKeywords().stream()
                        .map(diaryKeyword -> diaryKeyword.getKeyword().getName())
                        .limit(2) // **최대 2개 선택**
                        .toList())
                .orElseGet(Collections::emptyList);

        // 랜덤 키워드 1개 추가
        String randomKeyword = keywordRepository.findRandomKeyword().getName();

        List<String> finalKeywords = new ArrayList<>(diaryKeywordNames);
        finalKeywords.add(randomKeyword);

        return finalKeywords;
    }
}

