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

    // 유저별 완료한 챌린지 내역 조회
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId AND uc.completed = true")
    List<UserChallenge> findCompletedUserChallengesByUserId(@Param("userId") Long userId);

    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND (:dtype IS NULL OR uc.dtype = :dtype) " +
            "AND uc.completed = :completed")
    List<UserChallenge> findChallengesByCompletionStatus(
            @Param("userId") Long userId,
            @Param("dtype") ChallengeType dtype,
            @Param("completed") Boolean completed);

}




