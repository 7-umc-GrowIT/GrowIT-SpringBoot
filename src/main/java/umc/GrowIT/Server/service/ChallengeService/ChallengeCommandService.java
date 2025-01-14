package umc.GrowIT.Server.service.ChallengeService;

public interface ChallengeCommandService {
    void markChallengeAsCompleted(Long userId, Long challengeId);
}
