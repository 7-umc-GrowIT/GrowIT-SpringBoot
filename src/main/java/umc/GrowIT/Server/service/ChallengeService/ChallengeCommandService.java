package umc.GrowIT.Server.service.ChallengeService;

import org.springframework.web.bind.annotation.RequestParam;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeCommandService {
    void markChallengeAsCompleted(Long userId, Long challengeId);
    ChallengeResponseDTO.AddProofDTO createChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest);
    ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long challengeId);
    ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long challengeId, ChallengeRequestDTO.UpdateRequestDTO updateRequest);

}
