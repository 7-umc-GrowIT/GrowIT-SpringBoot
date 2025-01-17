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
import umc.GrowIT.Server.web.dto.CreditDTO.CreditResponseDTO;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentRequestDTO;
import umc.GrowIT.Server.web.dto.PaymentDTO.PaymentResponseDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserRequestDTO;
import umc.GrowIT.Server.web.dto.UserDTO.UserResponseDTO;

public interface UserSpecification {

    @GetMapping("/items")
    @Operation(summary = "보유중인 아이템 조회", description = "사용자가 보유한 아이템들을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")
    })
    ApiResponse<ItemResponseDTO.ItemListDTO> getUserItemList(
            @Parameter(description = "아이템 카테고리 (카테고리 명으로 전달)",
                    schema = @Schema(allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"}),
                    example = "BACKGROUND")
            @RequestParam ItemCategory category);


    @GetMapping("/credits")
    @Operation(summary = "현재 보유중인 크레딧 조회", description = "사용자가 현재 보유한 크레딧 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "MEMBER4001", description = "❌ 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<CreditResponseDTO.CurrentCreditDTO> getUserCredit();

    @GetMapping("/credits/total")
    @Operation(summary = "누적 크레딧 조회", description = "사용자의 누적 크레딧을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CREDIT4001", description = "❌ 크레딧 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<CreditResponseDTO.TotalCreditDTO> getUserTotalCredit();


    @PostMapping("/credits/payment")
    @Operation(summary = "크레딧 구매", description = "결제를 통해 크레딧을 구매합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAYMENT4001", description = "❌ 결제정보다 정확하지않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<PaymentResponseDTO> purchaseCredits(@RequestBody PaymentRequestDTO request);

    @PostMapping("/password")
    @Operation(summary = "비밀번호 재설정 API", description = "사용자가 비밀번호를 재설정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")
    })
    ApiResponse<UserRequestDTO.ResetPasswordRequestDTO> resetPassword(@RequestBody UserRequestDTO.ResetPasswordRequestDTO request);

    @PatchMapping("")
    @Operation(summary = "회원 탈퇴 API", description = "사용자가 자신의 계정을 삭제하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")
    })
    ApiResponse<UserResponseDTO.DeleteUserResponseDTO> deleteUser(@RequestBody UserRequestDTO.DeleteUserRequestDTO request);

    @PostMapping("/email")
    @Operation(summary = "인증 메일 전송 API", description = "사용자에게 인증 메일을 전송하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")
    })
    ApiResponse<UserResponseDTO.SendAuthEmailResponseDTO> sendAuthEmail(@RequestBody UserRequestDTO.SendAuthEmailRequestDTO request);

    @PostMapping("/verification")
    @Operation(summary = "인증 번호 확인 API", description = "사용자가 입력한 인증 번호를 확인하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")
    })
    ApiResponse<UserResponseDTO.VerifyAuthCodeResponseDTO> verifyAuthCode(@RequestBody UserRequestDTO.VerifyAuthCodeRequestDTO request);
}
