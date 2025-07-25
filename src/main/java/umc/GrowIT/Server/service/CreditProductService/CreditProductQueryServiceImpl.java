package umc.GrowIT.Server.service.CreditProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.converter.CreditProductConverter;
import umc.GrowIT.Server.domain.CreditProduct;
import umc.GrowIT.Server.repository.CreditProductRepository;
import umc.GrowIT.Server.web.dto.CreditProductDTO.CreditProductResponseDTO;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CreditProductQueryServiceImpl implements CreditProductQueryService {

    private final CreditProductRepository creditProductRepository;

    @Override // 크레딧 상품 리스트 조회
    public CreditProductResponseDTO.CreditProductListDTO getCreditProductList() {

        List<CreditProduct> creditProducts = creditProductRepository.findByIsActiveTrue();

        return CreditProductConverter.creditProductListDTO(creditProducts);
    }

    @Override
    public boolean validateProduct(String productId, Integer creditAmount, Integer paymentAmount){

        log.info("상품 검증 시작: productId={}, creditAmount={}, paymentAmount={}",
                productId, creditAmount, paymentAmount);

        // 1. 상품 존재 여부 확인

        // 2. 상품 활성화 여부 확인

        // 3. 크레딧 수량 검증  (클라이언트로 부터 requestBody로 전달받은 값들 검증 -> DB의 해당 productId 상품의 가격&크레딧 수량 일치하는지 검증)

        // 4. 결제 금액 검증



        return true;
    }
}
