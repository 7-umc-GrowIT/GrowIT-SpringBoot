package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;
import umc.GrowIT.Server.web.dto.WithdrawalDTO.WithdrawalResponseDTO;

import java.util.List;

public interface WithdrawalSpecification {

    @GetMapping("/reasons")
    @Operation(summary = "탈퇴이유 목록 조회 API", description = "회원탈퇴 시 선택할 수 있는 탈퇴이유 목록을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<List<WithdrawalResponseDTO.WithdrawalReasonDTO>> getWithdrawalReasons();
}
