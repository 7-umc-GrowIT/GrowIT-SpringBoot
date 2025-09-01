package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.CreditHistory;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.CreditSource;

import java.time.LocalDate;

public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {
    boolean existsByUserAndDateAndSource(User user, LocalDate date, CreditSource creditSource);
}
