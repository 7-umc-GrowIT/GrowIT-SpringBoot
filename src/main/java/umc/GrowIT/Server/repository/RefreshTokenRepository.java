package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
