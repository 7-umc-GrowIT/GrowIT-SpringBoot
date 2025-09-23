package umc.GrowIT.Server.web.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.service.itemService.ItemCommandService;
import umc.GrowIT.Server.service.itemService.ItemQueryServiceImpl;
import umc.GrowIT.Server.web.controller.specification.ItemSpecification;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipRequestDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;

@Tag(name="Item", description = "아이템 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController implements ItemSpecification {

    private final ItemQueryServiceImpl itemQueryServiceImpl;
    private final ItemCommandService itemCommandService;

    @Override
    @GetMapping("")
    public ApiResponse<ItemResponseDTO.ItemListDTO> getItemList(@AuthenticationPrincipal Long userId, ItemCategory category) {
        return ApiResponse.onSuccess(itemQueryServiceImpl.getItemList(category, userId));
    }

    //그로 아이템 착용/해제
    @Override
    @PatchMapping("/{itemId}")
    public ApiResponse<ItemEquipResponseDTO> updateItemStatus(@AuthenticationPrincipal Long userId, @PathVariable Long itemId, @RequestBody ItemEquipRequestDTO request) {
        String status = request.getStatus();

        return ApiResponse.onSuccess(itemCommandService.updateItemStatus(userId, itemId, status));
    }

    @Override
    @PostMapping("/{itemId}/purchase")
    public ApiResponse<ItemResponseDTO.PurchaseItemResponseDTO> purchaseItem(@AuthenticationPrincipal Long userId, @PathVariable Long itemId) {
        ItemResponseDTO.PurchaseItemResponseDTO purchasedItem = itemCommandService.purchase(itemId, userId);

        return ApiResponse.onSuccess(purchasedItem);
    }
}
