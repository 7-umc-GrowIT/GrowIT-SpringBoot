package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.ChallengeType;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByIdAndUserId (Long userChallengeId, Long userId);

    // 유저의 완료/미완료 챌린지 현황 조회
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND (:dtype IS NULL OR uc.dtype = :dtype) " +
            "AND uc.completed = :completed")
    List<UserChallenge> findChallengesByCompletionStatus(
            @Param("userId") Long userId,
            @Param("dtype") ChallengeType dtype,
            @Param("completed") Boolean completed);
}
