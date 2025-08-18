package umc.GrowIT.Server.web.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.domain.enums.ItemStatus;
import umc.GrowIT.Server.service.ItemService.ItemCommandService;
import umc.GrowIT.Server.service.ItemService.ItemQueryServiceImpl;
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
    public ApiResponse<ItemResponseDTO.ItemListDTO> getItemList(ItemCategory category) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id

        return ApiResponse.onSuccess(itemQueryServiceImpl.getItemList(category, userId));

    }

    //그로 아이템 착용/해제
    @Override
    @PatchMapping("/{itemId}")
    public ApiResponse<ItemEquipResponseDTO> updateItemStatus(Long itemId, ItemEquipRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        String status = request.getStatus();

        return ApiResponse.onSuccess(itemCommandService.updateItemStatus(userId, itemId, status));
    }

    @Override
    @PostMapping("/{itemId}/purchase")
    public ApiResponse<ItemResponseDTO.PurchaseItemResponseDTO> purchaseItem(Long itemId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal(); //사용자 식별 id

        ItemResponseDTO.PurchaseItemResponseDTO purchasedItem = itemCommandService.purchase(itemId, userId);

        return ApiResponse.onSuccess(purchasedItem);
    }
}
