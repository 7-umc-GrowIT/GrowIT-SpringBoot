package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.ChallengeKeyword;
import umc.GrowIT.Server.domain.Keyword;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeKeywordRepository extends JpaRepository<ChallengeKeyword, Long> {
    @Query("SELECT ck FROM ChallengeKeyword ck JOIN FETCH ck.challenge WHERE ck.keyword = :keyword")
    List<ChallengeKeyword> findByKeywordWithChallenge(@Param("keyword") Keyword keyword);

    @Query("SELECT ck.challenge.id FROM ChallengeKeyword ck " +
            "WHERE ck.keyword.id IN (" +
            "    SELECT dk.keyword.id FROM DiaryKeyword dk " +
            "    WHERE dk.diary.user.id = :userId " +
            "    AND dk.diary.date = :today" +
            ")")
    List<Long> findChallengeIdsByTodayDiaryKeywords(@Param("userId") Long userId, @Param("today") LocalDate today);

}
