package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.OAuthAccount;

import java.util.Optional;

public interface OAuthAccountRepository extends JpaRepository<OAuthAccount, Long> {
    @EntityGraph(attributePaths = {"user"})
    Optional<OAuthAccount> findByProviderId(Long providerId);

    boolean existsByProviderId(Long providerId);
}
