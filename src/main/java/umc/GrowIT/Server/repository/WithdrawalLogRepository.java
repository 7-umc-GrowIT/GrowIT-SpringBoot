package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.WithdrawalLog;

public interface WithdrawalLogRepository extends JpaRepository<WithdrawalLog, Long> {
}
