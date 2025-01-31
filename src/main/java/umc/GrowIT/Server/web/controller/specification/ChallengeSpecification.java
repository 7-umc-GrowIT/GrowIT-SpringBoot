package umc.GrowIT.Server.web.controller.specification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeSpecification {

    @GetMapping("summary")
    @Operation(summary = "챌린지 홈 조회 API", description = "사용자의 챌린지 홈 화면에 보여질 챌린지 요약 정보를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ChallengeHomeDTO> getChallengeHome();

    @GetMapping("/")
    @Operation(summary = "챌린지 현황 조회 API", description = "챌린지의 진행 상태(미완료/완료 등)를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ChallengeStatusListDTO> getChallengeStatus(
            @RequestParam(required = false) UserChallengeType dtype,
            @RequestParam(required = false) Boolean completed);



    @PostMapping("{userChallengeId}/select")
    @Operation(summary = "선택된 챌린지 저장 API", description = "사용자가 선택한 챌린지를 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.SelectChallengeDTO> selectChallenge(@PathVariable Long challengeId);

    @PostMapping("{userChallengeId}/prove")
    @Operation(summary = "챌린지 인증 작성 API", description = "챌린지 인증을 작성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> createChallengeProof(@PathVariable Long userChallengeId, @RequestBody ChallengeRequestDTO.ProofRequestDTO proofRequest);

    @GetMapping("{userChallengeId}")
    @Operation(summary = "챌린지 인증 내용 조회 API", description = "특정 챌린지의 인증 내용을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> getChallengeProofDetails(@PathVariable Long userChallengeId);

    @PatchMapping("{userChallengeId}")
    @Operation(summary = "챌린지 인증 수정 API", description = "챌린지 인증을 수정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })

    ApiResponse<ChallengeResponseDTO.ModifyProofDTO> updateChallengeProof(@PathVariable Long userChallengeId, @RequestBody ChallengeRequestDTO.ProofRequestDTO updateRequest);


    @DeleteMapping("{userChallengeId}")
    @Operation(
            summary = "사용자 챌린지 삭제 API",
            description = "사용자의 특정 챌린지를 삭제하는 API입니다.<br>" +
                    "사용자 챌린지 ID를 path variable로 전달받아 해당 사용자 챌린지를 삭제합니다.<br>" +
                    "❗Request Header에 JWT Access Token 값을 넣어야 합니다.❗"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "⭕ SUCCESS"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4002", description = "❌ 이메일 또는 패스워드가 일치하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4001", description = "❌ 사용자 챌린지가 존재하지 않습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "UC4002", description = "❌ 완료된 챌린지는 삭제가 불가합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "❌ BAD, 잘못된 요청", content = @Content(schema = @Schema(implementation = ApiResponse.class)))

    })
    @Parameter(name = "userChallengeId", description = "삭제할 사용자 챌린지의 ID", required = true)
    ApiResponse<ChallengeResponseDTO.DeleteChallengeResponseDTO> deleteChallenge(@PathVariable("userChallengeId") Long userChallengeId);
}
