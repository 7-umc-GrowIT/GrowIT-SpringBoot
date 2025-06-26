package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.CreditProductService.CreditProductQueryService;
import umc.GrowIT.Server.web.controller.specification.CreditProductSpecification;
import umc.GrowIT.Server.web.dto.CreditProductDTO.CreditProductResponseDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentRequestDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentResponseDTO;

@Tag(name = "CreditProduct", description = "크레딧 상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class CreditProductController implements CreditProductSpecification {

    private CreditProductQueryService creditProductQueryService;


    //활성화된 크레딧 상품 리스트 조회
    @GetMapping("/credits")
    public ApiResponse<CreditProductResponseDTO.CreditProductListDTO> getCreditProductList() {

        CreditProductResponseDTO.CreditProductListDTO creditProductList = creditProductQueryService.getCreditProductList();

        // ApiResponse로 감싸서 반환
        return ApiResponse.onSuccess(creditProductList);
    }


    @PostMapping("/credits/payment")
    public ApiResponse<PaymentResponseDTO> purchaseCredits(@RequestBody PaymentRequestDTO request){

        return null;
    }
}
