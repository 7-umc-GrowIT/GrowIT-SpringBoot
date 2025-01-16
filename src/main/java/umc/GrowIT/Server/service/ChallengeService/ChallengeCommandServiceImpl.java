package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.repository.ChallengeRepository;
import umc.GrowIT.Server.repository.UserChallengeRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;

    @Override
    public void markChallengeAsCompleted(Long userId, Long challengeId) {
        Challenge challenge = challengeRepository.findByIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다."));

        // 챌린지를 완료 상태로 변경
        challenge.markAsCompleted();

        // 변경된 챌린지 저장
        challengeRepository.save(challenge);
    }

    @Override
    @Transactional
    public ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long challengeId, Long userId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {
        // 1. 유저 및 챌린지 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다."));

        // 2. UserChallenge 생성
        UserChallenge userChallenge = UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .certificationImage(proofRequest.getCertificationImage())
                .thoughts(proofRequest.getThoughts())
                .completed(true)
                .build();

        userChallengeRepository.save(userChallenge);

        // 3. Challenge의 completed 상태를 true로 변경
        if (!challenge.isCompleted()) {
            challenge.markAsCompleted(); // Challenge 엔티티의 markAsCompleted 메서드 호출
            challengeRepository.save(challenge); // 변경사항 저장
        }

        // 4. 응답 DTO 반환
        return ChallengeResponseDTO.ProofDetailsDTO.builder()
                .challengeId(challenge.getId())
                .certificationImage(userChallenge.getCertificationImage())
                .thoughts(userChallenge.getThoughts())
                .completed(userChallenge.isCompleted())
                .build();
    }
}

