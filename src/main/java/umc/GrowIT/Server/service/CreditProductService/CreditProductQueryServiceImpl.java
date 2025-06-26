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

}
