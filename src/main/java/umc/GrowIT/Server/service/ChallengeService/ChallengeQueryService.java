package umc.GrowIT.Server.service.ChallengeService;

import umc.GrowIT.Server.domain.enums.ChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;

public interface ChallengeQueryService {
    int getTotalCredits(Long userId);
    int getTotalDiaries(Long userId);
    String getUserDate(Long userId);
    ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId);
    ChallengeResponseDTO.ChallengeStatusListDTO getChallengeStatus(Long userId, ChallengeType dtype, Boolean completed);
    ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long userId, Long challengeId);
}
