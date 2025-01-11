package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.Term;

public interface TermRepository extends JpaRepository<Term, Long> {
}
