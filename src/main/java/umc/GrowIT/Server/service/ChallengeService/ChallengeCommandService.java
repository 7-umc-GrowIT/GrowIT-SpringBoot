package umc.GrowIT.Server.service.ChallengeService;

import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeCommandService {
    ChallengeResponseDTO.SelectChallengeDTO selectChallenge(Long userId, Long challengeId);
    ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest);
    ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest);
    ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId);
}
