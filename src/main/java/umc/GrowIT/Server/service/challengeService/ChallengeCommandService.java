package umc.GrowIT.Server.service.challengeService;

import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;
import java.util.Map;

public interface ChallengeCommandService {

    void selectChallenges(Long userId, List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList);
    ChallengeResponseDTO.ProofPresignedUrlResponseDTO createChallengePresignedUrl(Long userId, ChallengeRequestDTO.ProofRequestPresignedUrlDTO request);
    void createChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest);
    void updateChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest);
    ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId);
}
