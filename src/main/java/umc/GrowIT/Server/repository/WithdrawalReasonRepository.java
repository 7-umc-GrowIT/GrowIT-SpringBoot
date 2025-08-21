package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.GrowIT.Server.domain.WithdrawalReason;

public interface WithdrawalReasonRepository extends JpaRepository<WithdrawalReason, Long> {
}
