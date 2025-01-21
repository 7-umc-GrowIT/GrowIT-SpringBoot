package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ChallengeHandler;
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

import java.util.Optional;

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

    // 팰린지 인증 작성
    @Override
    @Transactional
    public ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {
        // 유저 및 챌린지 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));

        // 기존 UserChallenge 조회 및 인증 상태 확인
        userChallengeRepository.findByIdAndUserId(userId, challengeId)
                .ifPresent(existingChallenge -> {
                    if (existingChallenge.isCompleted()) {
                        throw new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_ALREADY_EXISTS);
                    }
                });

        // UserChallenge 생성 및 저장
        UserChallenge userChallenge = ChallengeConverter.createUserChallenge(user, challenge, proofRequest);
        userChallenge.setCompleted(true); // 인증 완료로 설정
        userChallengeRepository.save(userChallenge);

        return ChallengeConverter.toProofDetailsDTO(challenge, userChallenge);
    }

    // 챌린지 인증 내역 조회
    @Override
    @Transactional(readOnly = true)
    public ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long challengeId) {
        // 챌린지 조회
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));

        // 인증 내역(UserChallenge) 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeId(challengeId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_NOT_EXISTS));

        return ChallengeConverter.toChallengeProofDetailsDTO(challenge, userChallenge);
    }

    public ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId) {

        // 1. userId와 userChallengeId를 통해 조회하고 없으면 오류
        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        // 2. 진행 중(false)인 챌린지인지 체크, 완료(true)한 챌린지면 오류
        if(userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.USER_CHALLENGE_COMPLETE);
        }

        // 3. 삭제
        userChallengeRepository.deleteById(userChallengeId);

        // 4. converter 작업
        return ChallengeConverter.toDeletedUserChallenge(userChallenge);
    }
}
