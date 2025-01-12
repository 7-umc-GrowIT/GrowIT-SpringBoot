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
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentRequestDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentResponseDTO;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final CreditQueryServiceImpl creditQueryService;

    @GetMapping("/items")
    @Operation(summary = "보유중인 아이템 조회", description = "사용자가 보유한 아이템들을 조회합니다.")
    public ApiResponse<ItemResponseDTO.ItemListDTO> getUserItemList(

            @Parameter(description = "아이템 카테고리 (카테고리 명으로 전달)",
                    schema = @Schema(allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"}),
                    example = "BACKGROUND")
            @RequestParam ItemCategory category)
    {
        return ApiResponse.onSuccess(null);
    }



    @GetMapping("/credits")
    @Operation(summary = "보유중인 크레딧 조회", description = "사용자가 현재 보유한 크레딧 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "COMMON200",
                    description = "성공"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "MEMBER4001",
                    description = "사용자가 없습니다.",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))
            )
    })
    public ApiResponse<CreditResponseDTO.CreditDTO> getUserCredit() {
        // TODO: Security Context에서 현재 로그인한 사용자의 ID를 가져오는 로직 필요
        Long userId = 1L; // 임시로 1L 사용
        return ApiResponse.onSuccess(creditQueryService.getCurrentCredit(userId));
    }



    @GetMapping("/credits/total")
    @Operation(summary = "누적 크레딧 조회", description = "사용자의 누적 크레딧을 조회합니다.")
    public ApiResponse<CreditResponseDTO.CreditDTO> getUserTotalCredit(

    )
    {
        return ApiResponse.onSuccess(null);
    }




    @PostMapping("/credits/payment")
    @Operation(
            summary = "크레딧 구매",
            description = "결제를 통해 크레딧을 구매합니다."
    )
    public ApiResponse<PaymentResponseDTO> purchaseCredits(
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "결제 정보",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PaymentRequestDTO.class)
                    )
            ) PaymentRequestDTO request
    ) {
        return ApiResponse.onSuccess(null);  // 실제 구현은 service에서 처리
    }
}
