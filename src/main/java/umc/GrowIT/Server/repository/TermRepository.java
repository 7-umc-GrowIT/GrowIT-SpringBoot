package umc.GrowIT.Server.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import umc.GrowIT.Server.domain.Term;

public interface TermRepository extends JpaRepository<Term, Long> {
    @Query("SELECT COUNT(t) FROM Term t WHERE t.effectiveDate <= :today AND (t.expirationDate IS NULL OR t.expirationDate >= :today)")
    int countByDate(LocalDateTime today);

    @Query("SELECT t FROM Term t WHERE t.effectiveDate <= :today AND (t.expirationDate IS NULL OR t.expirationDate >= :today)")
    List<Term> findAllByDate(LocalDateTime today);
}
