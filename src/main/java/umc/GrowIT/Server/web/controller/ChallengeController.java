package umc.GrowIT.Server.web.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.GrowIT.Server.apiPayload.ApiResponse;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.service.challengeService.ChallengeCommandService;
import umc.GrowIT.Server.service.challengeService.ChallengeQueryService;
import umc.GrowIT.Server.web.controller.specification.ChallengeSpecification;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;
import java.util.Map;

@Tag(name = "Challenge", description = "챌린지 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/challenges")
public class ChallengeController implements ChallengeSpecification {

    private final ChallengeQueryService challengeQueryService;
    private final ChallengeCommandService challengeCommandService;

    @GetMapping("summary")
    public ApiResponse<ChallengeResponseDTO.ChallengeHomeDTO> getChallengeHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        return ApiResponse.onSuccess(challengeQueryService.getChallengeHome(userId));
    }

    @GetMapping("")
    public ApiResponse<ChallengeResponseDTO.ChallengeStatusPagedResponseDTO> getChallengeStatus(
            @RequestParam(required = false) UserChallengeType dtype,
            @RequestParam Boolean completed,
            @RequestParam Integer page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        // 서비스 호출
        ChallengeResponseDTO.ChallengeStatusPagedResponseDTO challengeStatusList = challengeQueryService.getChallengeStatus(userId, dtype, completed, page);

        // 성공 응답 반환
        return ApiResponse.onSuccess(challengeStatusList);
    }

    @PostMapping("select")
    public ApiResponse<Void> selectChallenges(@RequestBody List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        challengeCommandService.selectChallenges(userId, selectRequestList);
        return ApiResponse.onSuccess();
    }

    @PostMapping("presigned-url")
    public ApiResponse<ChallengeResponseDTO.ProofPresignedUrlResponseDTO> getProofPresignedUrl(@Valid @RequestBody ChallengeRequestDTO.ProofRequestPresignedUrlDTO request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        ChallengeResponseDTO.ProofPresignedUrlResponseDTO result = challengeCommandService.createChallengePresignedUrl(userId, request);

        return ApiResponse.onSuccess(result);
    }

    @PostMapping("{userChallengeId}")
    public ApiResponse<Void> createChallengeProof(@PathVariable Long userChallengeId, @RequestBody ChallengeRequestDTO.ProofRequestDTO proofRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        challengeCommandService.createChallengeProof(userId, userChallengeId, proofRequest);
        return ApiResponse.onSuccess();
    }

    @GetMapping("{userChallengeId}")
    public ApiResponse<ChallengeResponseDTO.ProofDetailsDTO> getChallengeProofDetails(@PathVariable Long userChallengeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        ChallengeResponseDTO.ProofDetailsDTO response = challengeQueryService.getChallengeProofDetails(userId, userChallengeId);
        return ApiResponse.onSuccess(response);
    }

    @PatchMapping("{userChallengeId}")
    public ApiResponse<Void> updateChallengeProof(@PathVariable("userChallengeId") Long userChallengeId, @RequestBody(required = false) ChallengeRequestDTO.ProofRequestDTO updateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();
        challengeCommandService.updateChallengeProof(userId, userChallengeId, updateRequest);
        return ApiResponse.onSuccess();
    }

    @DeleteMapping("{userChallengeId}")
    public ApiResponse<ChallengeResponseDTO.DeleteChallengeResponseDTO> deleteChallenge(@PathVariable("userChallengeId") Long userChallengeId) {
        //AccessToken에서 userId 추출
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) authentication.getPrincipal();

        ChallengeResponseDTO.DeleteChallengeResponseDTO deleteChallenge = challengeCommandService.delete(userChallengeId, userId);
        return ApiResponse.onSuccess(deleteChallenge);
    }
}