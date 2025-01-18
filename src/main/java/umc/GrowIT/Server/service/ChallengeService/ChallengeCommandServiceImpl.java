package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.repository.ChallengeRepository;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final ChallengeRepository challengeRepository;

    @Override
    public void markChallengeAsCompleted(Long userId, Long challengeId) {
        Challenge challenge = challengeRepository.findByIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다."));

        // 챌린지를 완료 상태로 변경
        challenge.markAsCompleted();

        // 변경된 챌린지 저장
        challengeRepository.save(challenge);
    }
}
