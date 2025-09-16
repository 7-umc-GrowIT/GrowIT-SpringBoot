package umc.GrowIT.Server.service.challengeService;

import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeCommandService {

    void selectChallenges(Long userId, ChallengeRequestDTO.SelectChallengesRequestDTO selectRequestList);
    ChallengeResponseDTO.ProofPresignedUrlResponseDTO createChallengePresignedUrl(Long userId, ChallengeRequestDTO.ProofRequestPresignedUrlDTO request);
    ChallengeResponseDTO.CreateProofDTO createChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest);
    void updateChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest);
    void delete(Long userChallengeId, Long userId);
}
