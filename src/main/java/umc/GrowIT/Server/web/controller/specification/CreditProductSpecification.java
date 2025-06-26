package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.CreditProductDTO.CreditProductResponseDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentRequestDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentResponseDTO;

public interface CreditProductSpecification {

    @Operation(summary = "크레딧 상품 리스트 조회", description = "사용자가 구매 가능한 크레딧 상품 리스트를 조회합니다")
    @ApiResponses({@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")})
    @GetMapping("/credits")
    public ApiResponse<CreditProductResponseDTO.CreditProductListDTO> getCreditProductList();


    @Operation(summary = "크레딧 구매", description = "결제를 통해 크레딧을 구매합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAYMENT4001", description = "❌ 영수증 검증에 실패했습니다.")})
    @PostMapping("/credits/payment")
    public ApiResponse<PaymentResponseDTO> purchaseCredits(@RequestBody PaymentRequestDTO request);
}
