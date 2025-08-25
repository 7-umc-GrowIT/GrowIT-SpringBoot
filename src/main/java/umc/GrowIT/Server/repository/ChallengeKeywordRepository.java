package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = """
        DELETE ck FROM challenge_keyword ck 
        INNER JOIN user_challenge uc ON ck.challenge_id = uc.challenge_id 
        WHERE uc.user_id = :userId
        """, nativeQuery = true)
    void deleteByUserIdNative(@Param("userId") Long userId);
}
