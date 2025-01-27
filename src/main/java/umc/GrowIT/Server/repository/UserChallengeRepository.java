package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.UserChallenge;

import java.util.Optional;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    Optional<UserChallenge> findByIdAndUserId (Long userChallengeId, Long userId);
}
