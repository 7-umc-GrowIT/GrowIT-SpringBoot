package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.service.CreditService.CreditQueryServiceImpl;
import umc.GrowIT.Server.web.controller.specification.UserSpecification;
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentRequestDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

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
        Long userId = 13L; // 임시로 13L 송진우 사용
        return ApiResponse.onSuccess(creditQueryService.getCurrentCredit(userId));
    }

    @Override
    public ApiResponse<CreditResponseDTO.TotalCreditDTO> getUserTotalCredit() {
        Long userId = 13L; // 임시로 13L 송진우 사용
        return ApiResponse.onSuccess(creditQueryService.getTotalCredit(userId));
    }

    @Override
    public ApiResponse<PaymentResponseDTO> purchaseCredits(PaymentRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @Override
    public ApiResponse<UserRequestDTO.ResetPasswordRequestDTO> resetPassword(UserRequestDTO.ResetPasswordRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @Override
    public ApiResponse<UserResponseDTO.DeleteUserResponseDTO> deleteUser(UserRequestDTO.DeleteUserRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @Override
    public ApiResponse<UserResponseDTO.SendAuthEmailResponseDTO> sendAuthEmail(UserRequestDTO.SendAuthEmailRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @Override
    public ApiResponse<UserResponseDTO.VerifyAuthCodeResponseDTO> verifyAuthCode(UserRequestDTO.VerifyAuthCodeRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }
}