package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.CreditHistory;

public interface CreditHistoryRepository extends JpaRepository<CreditHistory, Long> {
}
