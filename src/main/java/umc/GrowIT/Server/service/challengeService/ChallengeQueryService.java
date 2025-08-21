package umc.GrowIT.Server.service.challengeService;

import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeQueryService {
    int getTotalCredits(Long userId);
    int getTotalDiaries(Long userId);
    String getDiaryDate(Long userId);
    ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId);
    ChallengeResponseDTO.ChallengeStatusPagedResponseDTO getChallengeStatus(Long userId, UserChallengeType dtype, Boolean completed, Integer page);
    ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long userId, Long userChallengeId);
}
