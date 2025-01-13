package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

@Tag(name = "Item", description = "Item 관련 API")
@RestController
@RequestMapping("/items")
public class ItemRestController {

    @PostMapping("/{itemId}/order")
    @Operation(
            summary = "아이템 주문 API",
            description = "특정 아이템을 주문하는 API입니다. 아이템 ID를 path variable로 전달받아 해당 아이템을 주문합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "itemId", description = "주문할 아이템의 ID", required = true)
    public ApiResponse<ItemResponseDTO.OrderItemResponseDTO> orderItem(@PathVariable("itemId") Long itemId) {
        return ApiResponse.onSuccess(null);
    }
}
