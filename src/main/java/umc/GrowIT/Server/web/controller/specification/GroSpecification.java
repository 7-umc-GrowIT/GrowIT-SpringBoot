package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.GroDTO.GroRequestDTO;
import umc.GrowIT.Server.web.dto.GroDTO.GroResponseDTO;

public interface GroSpecification {

    @PostMapping("")
    @Operation(summary = "그로 캐릭터 생성", description = "사용자의 그로 캐릭터를 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
    })
    ApiResponse<GroResponseDTO> createGro(@Valid @RequestBody GroRequestDTO request);
}
