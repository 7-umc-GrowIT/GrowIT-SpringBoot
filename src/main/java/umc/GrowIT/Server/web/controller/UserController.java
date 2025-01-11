package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    @GetMapping("/items")
    @Operation(summary = "보유중인 아이템 조회", description = "사용자가 보유한 아이템들을 조회합니다.")
    public ApiResponse<ItemResponseDTO.ItemListDTO> getUserItemList(

//            @Schema(description = "아이템 카테고리 (카테고리 명으로 전달)",
//                    allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"},
//                    example = "BACKGROUND")
            @Parameter(description = "아이템 카테고리 (카테고리 명으로 전달)",
                    schema = @Schema(allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"}),
                    example = "BACKGROUND")
            @RequestParam ItemCategory category)
    {
        return ApiResponse.onSuccess(null);
    }

}
