package umc.GrowIT.Server.repository.ChallengeRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    // 특정 챌린지 ID와 매핑된 인증 내역 조회
    Optional<UserChallenge> findByIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    @Query("SELECT uc FROM UserChallenge uc JOIN FETCH uc.challenge WHERE uc.user.id = :userId AND uc.completed = false")
    List<UserChallenge> findUserChallengesByUserId(@Param("userId") Long userId);

    // 오늘 날짜 기준으로 저장된 챌린지만 필터링
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.createdAt BETWEEN :startOfDay AND :endOfDay")
    List<UserChallenge> findTodayUserChallengesByUserId(
            @Param("userId") Long userId,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);



    // 1. 유저의 완료 또는 미완료 챌린지 조회 (dtype 무시)
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.completed = :completed")
    List<UserChallenge> findChallengesByCompletionStatus(
            @Param("userId") Long userId,
            @Param("completed") Boolean completed);

    // 2. 특정 dtype에 대해 미완료 챌린지 조회 (completed가 true인 챌린지는 제외)
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND uc.dtype = :dtype " +
            "AND uc.completed = false")  // 항상 미완료 챌린지만 조회
    List<UserChallenge> findChallengesByDtypeAndCompletionStatus(
            @Param("userId") Long userId,
            @Param("dtype") UserChallengeType dtype);

}




