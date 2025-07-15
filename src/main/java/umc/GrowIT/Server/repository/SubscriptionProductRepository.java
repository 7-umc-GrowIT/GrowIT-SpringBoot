package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.GrowIT.Server.domain.SubscriptionProduct;

@Repository
public interface SubscriptionProductRepository extends JpaRepository<SubscriptionProduct, Long>  {
}