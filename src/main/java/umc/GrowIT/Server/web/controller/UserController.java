package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.service.creditService.CreditQueryServiceImpl;
import umc.GrowIT.Server.service.itemService.ItemQueryServiceImpl;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.service.userService.UserQueryService;
import umc.GrowIT.Server.domain.enums.CreditTransactionType;
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
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;
    private final ItemQueryServiceImpl itemQueryServiceImpl;

    @Override
    @GetMapping("/items")
    public ApiResponse<ItemResponseDTO.ItemListDTO> getUserItemList(ItemCategory category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id

        return ApiResponse.onSuccess(itemQueryServiceImpl.getUserOwnedItemList(category, userId));
    }

    @Override
    @GetMapping("/credits")
    public ApiResponse<CreditResponseDTO.CurrentCreditDTO> getUserCredit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id
        return ApiResponse.onSuccess(creditQueryService.getCurrentCredit(userId));
    }

    @Override
    @GetMapping("/credits/total")
    public ApiResponse<CreditResponseDTO.TotalCreditDTO> getUserTotalCredit() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id
        return ApiResponse.onSuccess(creditQueryService.getTotalCredit(userId));
    }

    @Override
    @PostMapping("/credits/payment")
    public ApiResponse<PaymentResponseDTO> purchaseCredits(PaymentRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @Override
    @PatchMapping("/password")
    public ApiResponse<Void> findPassword(@RequestBody @Valid UserRequestDTO.PasswordDTO passwordDTO) {
        userCommandService.updatePassword(passwordDTO);
        return ApiResponse.onSuccess();
    }


    @Override
    @DeleteMapping("")
    public ApiResponse<Void> withdrawUser(
            @Valid @RequestBody UserRequestDTO.DeleteUserRequestDTO deleteUserRequestDTO
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        userCommandService.withdraw(userId, deleteUserRequestDTO);
        return ApiResponse.onSuccess();
    }

    @Override
    @GetMapping("/mypage")
    public ApiResponse<UserResponseDTO.MyPageDTO> getMyPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        UserResponseDTO.MyPageDTO result = userQueryService.getMyPage(userId);
        return ApiResponse.onSuccess(result);
    }

    @Override
    @GetMapping("/credits/history")
    public ApiResponse<UserResponseDTO.CreditHistoryResponseDTO> getCreditHistory(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam CreditTransactionType type,
            @RequestParam int page
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        return ApiResponse.onSuccess(userQueryService.getCreditHistory(userId, year, month, type, page));
    }
}