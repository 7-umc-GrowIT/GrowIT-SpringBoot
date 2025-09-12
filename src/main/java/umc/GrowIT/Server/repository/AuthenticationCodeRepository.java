package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.GrowIT.Server.domain.AuthenticationCode;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthenticationCodeRepository extends JpaRepository<AuthenticationCode, Integer> {

    @Modifying
    @Query("DELETE FROM AuthenticationCode ac WHERE ac.isVerified = false AND ac.expirationDate < :threshold")
    int deleteExpiredUnverifiedCodes(@Param("threshold") LocalDateTime threshold);

    Optional<AuthenticationCode> findByEmailAndIsVerifiedFalseAndExpirationDateAfter(String email, LocalDateTime now);
}
