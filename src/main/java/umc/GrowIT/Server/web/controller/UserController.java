package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.service.CreditService.CreditQueryServiceImpl;
import umc.GrowIT.Server.web.controller.specification.UserSpecification;
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentRequestDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentResponseDTO;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserSpecification {

    private final CreditQueryServiceImpl creditQueryService;

    @Override
    public ApiResponse<ItemResponseDTO.ItemListDTO> getUserItemList(ItemCategory category) {
        return ApiResponse.onSuccess(null);
    }

    @Override
    public ApiResponse<CreditResponseDTO.CurrentCreditDTO> getUserCredit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id
        return ApiResponse.onSuccess(creditQueryService.getCurrentCredit(userId));
    }

    @Override
    public ApiResponse<CreditResponseDTO.TotalCreditDTO> getUserTotalCredit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id
        return ApiResponse.onSuccess(creditQueryService.getTotalCredit(userId));
    }

    @Override
    public ApiResponse<PaymentResponseDTO> purchaseCredits(PaymentRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }
}