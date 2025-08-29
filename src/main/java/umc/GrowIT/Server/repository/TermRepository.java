package umc.GrowIT.Server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import umc.GrowIT.Server.domain.Term;
import umc.GrowIT.Server.domain.enums.TermStatus;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findAllByStatus(TermStatus status);

    int countByStatus(TermStatus status);
}
