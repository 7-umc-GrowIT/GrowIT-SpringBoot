package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.ChallengeType;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    // 특정 챌린지 ID와 매핑된 인증 내역 조회
    Optional<UserChallenge> findByIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    // 특정 유저의 모든 UserChallenge 조회
    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.id = :userId")
    List<UserChallenge> findUserChallengesByUserId(@Param("userId") Long userId);

    // 특정 유저의 인증 완료된 챌린지 조회
    @Query("SELECT uc FROM UserChallenge uc " +
            "JOIN uc.challenge c " +
            "WHERE uc.user.id = :userId " +
            "AND (:dtype IS NULL OR c.dtype = :dtype) " +
            "AND uc.completed = true")
    List<UserChallenge> findUserChallengesByStatusAndCompletion(
            @Param("userId") Long userId,
            @Param("dtype") ChallengeType dtype);


    // 특정 유저가 완료하지 않은 챌린지 조회
    @Query("SELECT c FROM Challenge c " +
            "WHERE c.id NOT IN (" +
            "  SELECT uc.challenge.id FROM UserChallenge uc " +
            "  WHERE uc.user.id = :userId AND uc.completed = true" +
            "  AND (:dtype IS NULL OR c.dtype = :dtype)" +
            ")")
    List<Challenge> findAvailableChallengesForUser(@Param("userId") Long userId, @Param("dtype") ChallengeType dtype);
}

