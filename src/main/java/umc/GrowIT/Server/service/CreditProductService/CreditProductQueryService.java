package umc.GrowIT.Server.service.CreditProductService;

import umc.GrowIT.Server.domain.CreditProduct;
import umc.GrowIT.Server.web.dto.CreditProductDTO.CreditProductResponseDTO;

import java.util.Optional;

public interface CreditProductQueryService {

    CreditProductResponseDTO.CreditProductListDTO getCreditProductList(); // 크레딧 상품 리스트 조회

    //정상적으로 등록되어있는 상품(creditProduct)인지 검증
    boolean validateProduct(String productId, Integer creditAmount, Integer paymentAmount);

}
