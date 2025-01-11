package umc.GrowIT.Server.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.dto.KakaoResponseDTO;

public interface KakaoSpecification {

    @GetMapping("/login/kakao")
    @Operation(summary = "카카오 소셜 로그인", description = "", security = @SecurityRequirement(name = ""))
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS")
    ApiResponse<KakaoResponseDTO.KakaoTokenDTO> kakaoLogin(@RequestParam(value = "code", required = false) String code);
}
