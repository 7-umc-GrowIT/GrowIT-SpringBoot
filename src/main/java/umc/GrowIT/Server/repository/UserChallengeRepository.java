package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.UserChallenge;

import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    // 특정 챌린지 ID와 매핑된 인증 내역 조회
    @Query("SELECT uc FROM UserChallenge uc WHERE uc.challenge.id = :challengeId")
    Optional<UserChallenge> findByChallengeId(@Param("challengeId") Long challengeId);
}
