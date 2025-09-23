package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.ChallengeKeyword;
import umc.GrowIT.Server.domain.Keyword;

import java.util.List;

public interface ChallengeKeywordRepository extends JpaRepository<ChallengeKeyword, Long> {
    @Query("SELECT ck FROM ChallengeKeyword ck JOIN FETCH ck.challenge WHERE ck.keyword = :keyword")
    List<ChallengeKeyword> findByKeywordWithChallenge(@Param("keyword") Keyword keyword);
}
