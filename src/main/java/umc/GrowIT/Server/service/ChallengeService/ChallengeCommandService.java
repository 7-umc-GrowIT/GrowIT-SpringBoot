package umc.GrowIT.Server.service.ChallengeService;

import org.springframework.web.bind.annotation.RequestParam;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeCommandService {
    void markChallengeAsCompleted(Long userId, Long challengeId);
    ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long challengeId, Long userId, ChallengeRequestDTO.ProofRequestDTO proofRequest);

}
