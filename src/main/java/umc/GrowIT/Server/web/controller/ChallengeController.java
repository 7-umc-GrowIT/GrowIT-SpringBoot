package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ChallengeType;
import umc.GrowIT.Server.service.ChallengeService.ChallengeCommandService;
import umc.GrowIT.Server.service.ChallengeService.ChallengeQueryService;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

@Tag(name = "Challenge", description = "챌린지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges")
public class ChallengeController {
    private final ChallengeQueryService challengeQueryService;
    private final ChallengeCommandService challengeCommandService;
    @GetMapping("/summary")
    @Operation(summary = "챌린지 홈 조회 API", description = "사용자의 챌린지 홈 화면에 보여질 챌린지 요약 정보를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 챌린지 홈 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD, 잘못된 요청"),
    })
    public ApiResponse<ChallengeResponseDTO.ChallengeHomeDTO> getChallengeHome(@RequestParam Long userId) {
        return ApiResponse.onSuccess(challengeQueryService.getChallengeHome(userId));
    }

    @GetMapping
    @Operation(summary = "챌린지 현황 조회 API", description = "챌린지의 진행 상태(미완료/완료 등)를 조회하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 챌린지 현황 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD, 잘못된 요청"),
    })
    public ApiResponse<ChallengeResponseDTO.ChallengeStatusListDTO> getChallengeStatus(
            @RequestParam Long userId,
            @RequestParam(required = false) ChallengeType status,
            @RequestParam(required = false) Boolean completed) {
        // 서비스 호출
        ChallengeResponseDTO.ChallengeStatusListDTO challengeStatusList = challengeQueryService.getChallengeStatus(userId, status, completed);

        // 성공 응답 반환
        return ApiResponse.onSuccess(challengeStatusList);
    }



    @PostMapping("/{challengeId}/select")
    @Operation(summary = "선택된 챌린지 저장 API", description = "사용자가 선택한 챌린지를 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 챌린지 저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD, 잘못된 요청"),
    })
    public ApiResponse<ChallengeResponseDTO> selectChallenge(@PathVariable Long challengeId) {
        return null;
    }

    @PostMapping("/{challengeId}/prove")
    @Operation(summary = "챌린지 인증 작성 API", description = "챌린지 인증을 작성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 챌린지 인증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD, 잘못된 요청"),
    })
    public ApiResponse<ChallengeResponseDTO.AddProofDTO> createChallengeProof(@RequestParam Long userId, @PathVariable Long challengeId, @RequestBody ChallengeRequestDTO.ProofRequestDTO proofRequest) {

        // 서비스 호출
        ChallengeResponseDTO.AddProofDTO response = challengeCommandService.createChallengeProof(userId, challengeId, proofRequest);

        // 성공 응답 반환
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{challengeId}")
    @Operation(summary = "챌린지 인증 내용 조회 API", description = "특정 챌린지의 인증 내용을 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 챌린지 인증 내용 조회 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD, 잘못된 요청"),
    })
    public ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> getChallengeProofDetails(@PathVariable Long challengeId) {
        ChallengeResponseDTO.ProofDetailsDTO response = challengeCommandService.getChallengeProofDetails(challengeId);
        return ApiResponse.onSuccess(response);
    }

    @PatchMapping("/{challengeId}")
    @Operation(summary = "챌린지 인증 수정 API", description = "챌린지 인증을 수정하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 챌린지 인증 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD, 잘못된 요청"),
    })
    public ApiResponse<ChallengeResponseDTO> updateChallengeProof(@PathVariable Long challengeId,
                                                                  @RequestBody ChallengeRequestDTO.UpdateRequestDTO updateRequest) {
        return null;
    }

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
