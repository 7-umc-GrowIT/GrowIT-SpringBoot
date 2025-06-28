package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.service.CreditProductService.CreditProductQueryService;
import umc.GrowIT.Server.service.PaymentService.PaymentCommandService;
import umc.GrowIT.Server.web.controller.specification.CreditProductSpecification;
import umc.GrowIT.Server.web.dto.CreditProductDTO.CreditProductResponseDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;

@Tag(name = "CreditProduct", description = "크레딧 상품 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class CreditProductController implements CreditProductSpecification {

    private CreditProductQueryService creditProductQueryService;
    private PaymentCommandService paymentCommandService;

    //활성화된 크레딧 상품 리스트 조회
    @GetMapping("/credits")
    public ApiResponse<CreditProductResponseDTO.CreditProductListDTO> getCreditProductList() {

        CreditProductResponseDTO.CreditProductListDTO creditProductList = creditProductQueryService.getCreditProductList();

        // ApiResponse로 감싸서 반환
        return ApiResponse.onSuccess(creditProductList);
    }


    //크레딧 구매정보 처리
    @PostMapping("/credits/payment")
    public ApiResponse<CreditPaymentResponseDTO> purchaseCredits(@RequestBody CreditPaymentRequestDTO request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); // 사용자 인가정보

        CreditPaymentResponseDTO result = paymentCommandService.processCreditPayment(userId, request);

        return ApiResponse.onSuccess(result);
    }
}
