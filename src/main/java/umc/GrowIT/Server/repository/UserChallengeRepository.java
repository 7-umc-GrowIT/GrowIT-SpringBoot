package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.UserChallenge;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
}
