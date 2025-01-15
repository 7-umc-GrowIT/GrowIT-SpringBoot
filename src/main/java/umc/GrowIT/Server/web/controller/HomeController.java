package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.HomeDTO.HomeResponseDTO;

@Tag(name = "Home", description = "홈화면 관련 API")
@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping()
    @Operation(
            summary = "홈 화면 조회 API",
            description = "홈 화면을 조회하는 API입니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<HomeResponseDTO.GetHomeResponseDTO> getHome() {
        return ApiResponse.onSuccess(null);
    }
}
