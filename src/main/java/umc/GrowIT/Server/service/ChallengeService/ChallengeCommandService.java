package umc.GrowIT.Server.service.ChallengeService;

import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

public interface ChallengeCommandService {

    ChallengeResponseDTO.SelectChallengeDTO selectChallenge(Long userId, Long challengeId, UserChallengeType dtype);
    ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest);
    ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest);
    ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId);
}
