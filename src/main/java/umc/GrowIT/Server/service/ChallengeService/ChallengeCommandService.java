package umc.GrowIT.Server.service.ChallengeService;

import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeCommandService {

    ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long userId, Long challengeId);
    ChallengeResponseDTO.ProofDetailsDTO updateChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest);
    ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId);
}
