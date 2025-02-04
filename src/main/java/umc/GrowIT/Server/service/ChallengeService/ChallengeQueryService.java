package umc.GrowIT.Server.service.ChallengeService;

import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeQueryService {
    int getTotalCredits(Long userId);
    int getTotalDiaries(Long userId);
    String getDiaryDate(Long userId);
    ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId);
    ChallengeResponseDTO.ChallengeStatusListDTO getChallengeStatus(Long userId, UserChallengeType dtype, Boolean completed);
    ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long userId, Long challengeId);
}
