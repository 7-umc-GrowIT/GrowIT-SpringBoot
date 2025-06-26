package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.CreditProduct;
import umc.GrowIT.Server.repository.CreditProductRepository;
import umc.GrowIT.Server.web.dto.CreditProductDTO.CreditProductResponseDTO;

import java.util.List;

public class CreditProductConverter {

    // 크레딧 상품 1개 리턴
    public static CreditProductResponseDTO.CreditProductDTO toCreditProductDTO(CreditProduct creditProduct) {

        return CreditProductResponseDTO.CreditProductDTO.builder()
                .id(creditProduct.getId())
                .name(creditProduct.getName())
                .description(creditProduct.getDescription())
                .price(creditProduct.getPrice())
                .description(creditProduct.getDescription())
                .creditAmount(creditProduct.getCreditAmount())
                .build();
    }
    
    // 크레딧 상품 리스트 리턴
    public static CreditProductResponseDTO.CreditProductListDTO creditProductListDTO(List<CreditProduct> creditProducts) {
        List<CreditProductResponseDTO.CreditProductDTO> productList = creditProducts.stream()
                .map(CreditProductConverter::toCreditProductDTO)
                .toList();

        return CreditProductResponseDTO.CreditProductListDTO.builder()
                .creditProducts(productList)
                .build();
    }
}
