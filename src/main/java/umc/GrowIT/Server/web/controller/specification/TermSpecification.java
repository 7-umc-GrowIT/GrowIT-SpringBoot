package umc.GrowIT.Server.web.controller.specification;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.TermDTO.TermResponseDTO;

public interface TermSpecification {

    @GetMapping("/terms")
    @Operation(summary = "약관 목록 조회", description = "", security = @SecurityRequirement(name = ""))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON_200", description = "⭕ SUCCESS")
    ApiResponse<List<TermResponseDTO.TermDTO>> getTerms();
}
