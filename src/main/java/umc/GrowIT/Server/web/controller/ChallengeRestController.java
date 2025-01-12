package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

@Tag(name = "Challenge", description = "Challenge 관련 API")
@RestController
@RequestMapping("/challenges")
public class ChallengeRestController {

    @DeleteMapping("{challengeId}")
    @Operation(
            summary = "챌린지 삭제 API",
            description = "특정 챌린지를 삭제하는 API입니다. 챌린지 ID를 path variable로 전달받아 해당 챌린지를 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameter(name = "challengeId", description = "삭제할 챌린지의 ID", required = true)
    public ApiResponse<ChallengeResponseDTO.DeleteChallengeResponseDTO> deleteChallenge(@PathVariable("challengeId") Long challengeId) {
        return ApiResponse.onSuccess(null);
    }
}
