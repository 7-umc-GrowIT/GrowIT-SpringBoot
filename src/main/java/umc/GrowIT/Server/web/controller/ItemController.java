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
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipRequestDTO;
import umc.GrowIT.Server.web.dto.ItemEquipDTO.ItemEquipResponseDTO;


@Tag(name="Item", description = "아이템 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    @GetMapping("")
    public ApiResponse<ItemResponseDTO.ItemListDTO> getItemList(
            @Parameter(description = "아이템 카테고리 (카테고리 명으로 전달)",
                    schema = @Schema(allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"}),
                    example = "BACKGROUND")
            @RequestParam ItemCategory category)
    {
        return ApiResponse.onSuccess(null);
    }



    @PatchMapping("/{itemId}")
    @Operation(
            summary = "아이템 착용 상태 변경",
            description = "사용자가 보유한 아이템의 착용 상태를 변경합니다."
    )
    public ApiResponse<ItemEquipResponseDTO> updateItemStatus(

            @Parameter(description = "아이템 ID", example = "1")
            @PathVariable(name = "itemId") Long itemId,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "아이템 착용 상태 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = ItemEquipRequestDTO.class)
                    )
            ) ItemEquipRequestDTO request
    ) {
        return ApiResponse.onSuccess(null);
    }
}
