package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.util.List;
import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    // 특정 챌린지 ID와 매핑된 인증 내역 조회
    Optional<UserChallenge> findByIdAndUserId(@Param("challengeId") Long challengeId, @Param("userId") Long userId);

    // 유저의 완료/미완료 챌린지 현황 조회
    @Query("SELECT uc FROM UserChallenge uc " +
            "WHERE uc.user.id = :userId " +
            "AND (:dtype IS NULL OR uc.dtype = :dtype) " +
            "AND uc.completed = :completed")
    List<UserChallenge> findChallengesByCompletionStatus(
            @Param("userId") Long userId,
            @Param("dtype") UserChallengeType dtype,
            @Param("completed") Boolean completed);

    // 선택한 챌린지를 이미 저장하였는지 조회하는 메서드
    boolean ChallengeExists(Long userId, Long challengeId, UserChallengeType dtype);

}




