package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.UserChallenge;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    // 특정 사용자 ID의 총 크레딧 수 조회
    @Query("SELECT u.totalCredit FROM User u WHERE u.id = :userId")
    Integer getTotalCreditsByUserId(Long userId);

    // 특정 사용자 ID의 작성된 일기 수 조회
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.user.id = :userId")
    int getTotalDiariesByUserId(Long userId);

    @Query("SELECT c FROM Challenge c WHERE c NOT IN :dailyChallenges ORDER BY FUNCTION('RAND') LIMIT 1")
    Challenge findRandomRemainingChallenge(@Param("dailyChallenges") List<Challenge> dailyChallenges);
}
