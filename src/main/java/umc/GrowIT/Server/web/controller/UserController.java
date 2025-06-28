package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.service.CreditService.CreditQueryServiceImpl;
import umc.GrowIT.Server.service.ItemService.ItemQueryServiceImpl;
import umc.GrowIT.Server.service.userService.UserCommandService;
import umc.GrowIT.Server.web.controller.specification.UserSpecification;
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentRequestDTO;
import umc.GrowIT.Server.web.dto.CreditPaymentDTO.CreditPaymentResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

@Tag(name = "User", description = "사용자 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserSpecification {

    private final CreditQueryServiceImpl creditQueryService;
    private final UserCommandService userCommandService;
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
    @PatchMapping("/password")
    public ApiResponse<Void> findPassword(@RequestBody @Valid UserRequestDTO.PasswordDTO passwordDTO) {
        userCommandService.updatePassword(passwordDTO);
        return ApiResponse.onSuccess();
    }

    @Override
    @PatchMapping("")
    public ApiResponse<UserResponseDTO.DeleteUserResponseDTO> deleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        UserResponseDTO.DeleteUserResponseDTO deleteUser = userCommandService.delete(userId);
        return ApiResponse.onSuccess(deleteUser);
    }
}