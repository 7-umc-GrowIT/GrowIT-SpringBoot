package umc.GrowIT.Server.web.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.service.ItemService.ItemQueryServiceImpl;
import umc.GrowIT.Server.web.controller.specification.ItemSpecification;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipRequestDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;

@Tag(name="Item", description = "아이템 관련 API")
@RestController
@RequiredArgsConstructor
public class ItemController implements ItemSpecification {

    private final ItemQueryServiceImpl itemQueryServiceImpl;

    @Override
    public ApiResponse<ItemResponseDTO.ItemListDTO> getItemList(ItemCategory category) {
        return ApiResponse.onSuccess(itemQueryServiceImpl.getItemList(category, 13L));
        //우선 id가 13인 사용자로 하드코딩하고 후에 사용자 토큰을 통해 사용자 판변하여 userId 전달
    }

    @Override
    public ApiResponse<ItemEquipResponseDTO> updateItemStatus(Long itemId, ItemEquipRequestDTO request) {
        return ApiResponse.onSuccess(null);
    }

    @Override
    public ApiResponse<ItemResponseDTO.OrderItemResponseDTO> orderItem(Long itemId) {
        return ApiResponse.onSuccess(null);
    }
}
