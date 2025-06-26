package umc.GrowIT.Server.web.dto.CreditProductDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CreditProductResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditProductDTO {
        private Long id;
        private String productId; // App Store에서 사용하는 상품 ID
        private String name; // 상품 이름
        private Integer price; // 상품 가격
        private String description; // 상품 설명
        private Integer creditAmount; // 크레딧 수량
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditProductListDTO { // 크레딧 상품 리스트

        private List<CreditProductDTO> creditProducts;
    }

}
