package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.ChallengeType;
import umc.GrowIT.Server.service.ChallengeService.ChallengeCommandService;
import umc.GrowIT.Server.service.ChallengeService.ChallengeQueryService;
import umc.GrowIT.Server.web.controller.specification.ChallengeSpecification;
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
public class ChallengeController implements ChallengeSpecification {

    private final ChallengeQueryService challengeQueryService;
    private final ChallengeCommandService challengeCommandService;

    @GetMapping("/summary")
    public ApiResponse<ChallengeResponseDTO.ChallengeHomeDTO> getChallengeHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.onSuccess(challengeQueryService.getChallengeHome(userId));
    }

    @GetMapping
    public ApiResponse<ChallengeResponseDTO.ChallengeStatusListDTO> getChallengeStatus(
            @RequestParam(required = false) ChallengeType status,
            @RequestParam Boolean completed) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        // 서비스 호출
        ChallengeResponseDTO.ChallengeStatusListDTO challengeStatusList = challengeQueryService.getChallengeStatus(userId, status, completed);

        // 성공 응답 반환
        return ApiResponse.onSuccess(challengeStatusList);
    }

    @PostMapping("/{challengeId}/select")
    public ApiResponse<ChallengeResponseDTO> selectChallenge(@PathVariable Long challengeId) {
        return null;
    }

    @PostMapping("/{challengeId}/prove")
    public ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> createChallengeProof(@PathVariable Long challengeId, @RequestBody ChallengeRequestDTO.ProofRequestDTO proofRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        // 서비스 호출
        ChallengeResponseDTO.ProofDetailsDTO response = challengeCommandService.createChallengeProof(userId, challengeId, proofRequest);

        // 성공 응답 반환
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/{challengeId}")
    public ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> getChallengeProofDetails(@PathVariable Long challengeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        ChallengeResponseDTO.ProofDetailsDTO response = challengeCommandService.getChallengeProofDetails(userId, challengeId);
        return ApiResponse.onSuccess(response);
    }

    @PatchMapping("/{challengeId}")
    public ApiResponse<ChallengeResponseDTO> updateChallengeProof(@PathVariable Long challengeId,
                                                                  @RequestBody ChallengeRequestDTO.UpdateRequestDTO updateRequest) {
        return null;
    }

    @DeleteMapping("{challengeId}")
    public ApiResponse<ChallengeResponseDTO.DeleteChallengeResponseDTO> deleteChallenge(@PathVariable("challengeId") Long userChallengeId) {
        // 임시로 사용자 ID 지정
        Long userId = 1L;

        ChallengeResponseDTO.DeleteChallengeResponseDTO deleteChallenge = challengeCommandService.delete(userChallengeId, userId);
        return ApiResponse.onSuccess(deleteChallenge);
    }
}
