package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.UserTerm;

import java.util.Optional;

public interface UserTermRepository extends JpaRepository<UserTerm, Long>{
    Optional<UserTerm> findByTermId(Long termId);
}
