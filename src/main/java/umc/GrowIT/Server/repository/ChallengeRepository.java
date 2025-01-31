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
    // 사용자 ID를 기반으로 추천 챌린지 조회
    @Query("SELECT c FROM Challenge c WHERE c.id NOT IN " +
            "(SELECT uc.challenge.id FROM UserChallenge uc WHERE uc.user.id = :userId)")
    List<Challenge> findRecommendedChallengesByUserId(Long userId);

    // 완료된 챌린지 ID 조회
    @Query("SELECT uc.challenge.id FROM UserChallenge uc WHERE uc.user.id = :userId AND uc.completed = true")
    List<Long> findCompletedChallengeIdsByUserId(Long userId);

    // 특정 사용자 ID의 총 크레딧 수 조회
    @Query("SELECT u.totalCredit FROM User u WHERE u.id = :userId")
    Integer getTotalCreditsByUserId(Long userId);

    // 특정 사용자 ID의 작성된 일기 수 조회
    @Query("SELECT COUNT(d) FROM Diary d WHERE d.user.id = :userId")
    int getTotalDiariesByUserId(Long userId);

    // 사용자 가입날짜 조회
    @Query("SELECT u.createdAt FROM User u WHERE u.id = :userId")
    Optional<LocalDate> findJoinDateByUserId(Long userId);

    // 챌린지 수정
    @Query("SELECT uc FROM UserChallenge uc WHERE uc.challenge.id = :challengeId")
    Optional<UserChallenge> findByChallengeId(@Param("challengeId") Long challengeId);

}
