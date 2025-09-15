package umc.GrowIT.Server.web.controller.specification;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.CreditTransactionType;
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS")
    })
    ApiResponse<ItemResponseDTO.ItemListDTO> getUserItemList(
            @Parameter(description = "아이템 카테고리 (카테고리 명으로 전달)",
                    schema = @Schema(allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"}),
                    example = "BACKGROUND")
            @RequestParam ItemCategory category);


    @GetMapping("/credits")
    @Operation(summary = "현재 보유중인 크레딧 조회", description = "사용자가 현재 보유한 크레딧 조회")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<CreditResponseDTO.CurrentCreditDTO> getUserCredit();


    @GetMapping("/credits/total")
    @Operation(summary = "누적 크레딧 조회", description = "사용자의 누적 크레딧을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CREDIT_404_01", description = "❌ 크레딧 정보를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<CreditResponseDTO.TotalCreditDTO> getUserTotalCredit();


    @PostMapping("/credits/payment")
    @Operation(summary = "크레딧 구매", description = "결제를 통해 크레딧을 구매합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAYMENT_400_01", description = "❌ 결제정보가 정확하지않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<PaymentResponseDTO> purchaseCredits(@RequestBody PaymentRequestDTO request);


    @GetMapping("/credits/history")
    @Operation(summary = "크레딧 기록 조회 API", description = "현재 사용되는 곳이 별도로 없는 테스트 용도의 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<UserResponseDTO.CreditHistoryResponseDTO> getCreditHistory (
            @Parameter(description = "조회 연도", example = "2025") @RequestParam Integer year,
            @Parameter(description = "조회 월", example = "9") @RequestParam Integer month,
            @Parameter(description = "조회 타입 (ALL, EARN, SPEND)", example = "ALL")
            @RequestParam(defaultValue = "ALL") CreditTransactionType type,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0")
            @RequestParam(defaultValue = "0") int page
    );


    @DeleteMapping("")
    @Operation(summary = "회원 탈퇴 API", description = "서비스에서 탈퇴하는 API입니다.<br>" +
            "실행 즉시, 사용자와 관련된 모든 데이터를 삭제하며 선택한 탈퇴사유와 가입일/탈퇴일을 기록합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "WITHDRAWAL_404_01", description = "❌ 존재하지 않는 탈퇴 사유입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Void> withdrawUser(@Valid @RequestBody UserRequestDTO.DeleteUserRequestDTO deleteUserRequestDTO);


    @PatchMapping("/password")
    @Operation(summary = "비밀번호 변경", description = "사용자의 비밀번호를 변경하는 API입니다.", security = @SecurityRequirement(name = "tempToken"))
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH_400_01", description = "❌ 이메일 인증을 완료해주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_403_01", description = "❌ 탈퇴한 회원입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PWD_400_01", description = "❌ 비밀번호 확인이 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PWD_400_02", description = "❌ 새로운 비밀번호를 입력해주세요.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<Void> updatePassword(@RequestBody @Valid UserRequestDTO.PasswordDTO passwordDTO);


    @GetMapping("/mypage")
    @Operation(summary = "마이페이지 조회 API", description = "마이페이지에서 사용자의 정보를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<UserResponseDTO.MyPageDTO> getMyPage();


    @GetMapping("/me/email")
    @Operation(summary = "로그인한 이메일 조회 API", description = "마이페이지에서 비밀번호 변경 시 사용하는 로그인한 이메일 조회 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER_401_01", description = "❌ 사용자를 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<UserResponseDTO.EmailResponseDTO> getMyEmail ();
}
