package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.domain.PaymentHistory;

@Repository
@Transactional
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long>{

    boolean existsPaymentHistoryById(String transactionId);

}
