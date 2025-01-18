package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipRequestDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;

public interface ItemSpecification {

    @GetMapping("/items")
    @Operation(summary = "카테고리별 아이템 조회", description = "상점이나 보유아이템 조회에서 카테고리를 선택했을 때 실행.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")
    })
    ApiResponse<ItemResponseDTO.ItemListDTO> getItemList(
            @Parameter(description = "아이템 카테고리 (카테고리 명으로 전달)",
                    schema = @Schema(allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"}),
                    example = "BACKGROUND")
            @RequestParam ItemCategory category);



    @PatchMapping("/items/{itemId}")
    @Operation(summary = "아이템 착용 상태 변경", description = "사용자가 보유한 아이템의 착용 상태를 변경합니다.")
    ApiResponse<ItemEquipResponseDTO> updateItemStatus(
            @Parameter(description = "아이템 ID", example = "1")
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "아이템 착용 상태 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ItemEquipRequestDTO.class)
                    )
            ) ItemEquipRequestDTO request
    );




    @PostMapping("/items/{itemId}/purchase")
    @Operation(summary = "아이템 구매 API", description = "특정 아이템을 구매하는 API입니다. 아이템 ID를 path variable로 전달받아 해당 아이템을 주문합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ITEM4001", description = "❌ 아이템을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CREDIT4002", description = "❌ 보유 크레딧이 부족합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "itemId", description = "주문할 아이템의 ID", required = true)
    ApiResponse<ItemResponseDTO.PurchaseItemResponseDTO> purchaseItem(@PathVariable("itemId") Long itemId);
}
