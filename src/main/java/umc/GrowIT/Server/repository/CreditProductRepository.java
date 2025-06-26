package umc.GrowIT.Server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import umc.GrowIT.Server.domain.CreditProduct;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditProductRepository extends JpaRepository<CreditProduct, Long> {

    // 상품 ID로 크레딧 상품 조회
    Optional<CreditProduct> findByProductId(String productId);

    // 활성화된 크레딧 상품 조회
    List<CreditProduct> findByIsActiveTrue();

}
