package umc.GrowIT.Server.service.ChallengeService;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.enums.ChallengeStatus;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;

public interface ChallengeQueryService {
    int getTotalCredits(Long userId);
    int getTotalDiaries(Long userId);
    String getUserDate(Long userId);
    ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId);
    ChallengeResponseDTO.ChallengeStatusListDTO getChallengeStatus(Long userId, Boolean completed);
}
