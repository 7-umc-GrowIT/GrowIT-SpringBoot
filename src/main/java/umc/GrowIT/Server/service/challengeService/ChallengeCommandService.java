package umc.GrowIT.Server.service.challengeService;

import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;

public interface ChallengeCommandService {

    ChallengeResponseDTO.SelectChallengeDTO selectChallenges(Long userId, List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList);
    ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest);
    ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest);
    ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId);
}
