package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ChallengeHandler;
import umc.GrowIT.Server.apiPayload.exception.UserChallengeHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.ChallengeConverter;
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
        Challenge challenge = challengeRepository.findByIdAndUserId(challengeId, userId).orElse(null);

        if(challenge == null) {
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND);
        }

        challenge.markAsCompleted();
        challengeRepository.save(challenge);
    }

    @Override
    @Transactional
    public ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {
        // 유저 및 챌린지 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));

        UserChallenge userChallenge = ChallengeConverter.createUserChallenge(user, challenge, proofRequest);
        userChallengeRepository.save(userChallenge);

        // 이미 완료된 챌린지인지 확인
        if (challenge.isCompleted()) {
            throw new IllegalStateException("이미 완료된 챌린지입니다.");
        }

        challenge.markAsCompleted();
        challengeRepository.save(challenge);

        return ChallengeConverter.toProofDetailsDTO(userChallenge);
    }

    public ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long challengeId, Long userId) {
        // 1. 사용자 조회하고 없으면 오류
        userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. 챌린지 조회하고 없으면 오류
        challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));

        // 3. userId와 challengeId를 통해 조회하고 없으면 오류
        UserChallenge userChallenge = userChallengeRepository.findByChallengeIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new UserChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        // 4. 진행 중(false)인 챌린지인지 체크, 완료(true)한 챌린지면 오류
        if(userChallenge.isCompleted()) {
            throw new UserChallengeHandler(ErrorStatus.USER_CHALLENGE_COMPLETE);
        }

        // 5. 삭제
        userChallengeRepository.delete(userChallenge);

        // 6. converter 작업
        return ChallengeConverter.toDeletedUserChallenge(userChallenge);
    }
}
