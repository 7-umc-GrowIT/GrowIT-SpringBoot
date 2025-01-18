package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        Challenge challenge = challengeRepository.findByIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다."));

        // 챌린지를 완료 상태로 변경
        challenge.markAsCompleted();

        // 변경된 챌린지 저장
        challengeRepository.save(challenge);
    }

    // 팰린지 인증 작성
    @Override
    @Transactional
    public ChallengeResponseDTO.AddProofDTO createChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {
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

        // 4. 응답 DTO 생성
        return ChallengeConverter.toChallengeResponseDTO(userChallenge);
    }

    // 챌린지 인증 내역 조회
    @Override
    @Transactional(readOnly = true)
    public ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long challengeId) {
        // 1. 챌린지 조회
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지를 찾을 수 없습니다."));

        // 2. 챌린지가 미완료 상태라면 예외 처리
        if (!challenge.isCompleted()) {
            throw new IllegalStateException("미완료 상태의 챌린지는 인증 내역을 볼 수 없습니다.");
        }

        // 3. 인증 내역(UserChallenge) 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeId(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지 인증 내역이 없습니다."));

        // 4. ProofDetailsDTO로 변환하여 반환
        return ChallengeConverter.toChallengeProofDetailsDTO(challenge, userChallenge);
    }

    @Override
    @Transactional
    public ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long challengeId, ChallengeRequestDTO.UpdateRequestDTO updateRequest) {
        // 1. 해당 챌린지 조회
        UserChallenge userChallenge = userChallengeRepository.findByChallengeId(challengeId)
                .orElseThrow(() -> new IllegalArgumentException("챌린지 인증 내역을 찾을 수 없습니다."));

        // 2. 인증 내용 수정
        if (updateRequest.getCertificationImage() != null) {
            userChallenge.setCertificationImage(updateRequest.getCertificationImage());
        }
        if (updateRequest.getThoughts() != null) {
            userChallenge.setThoughts(updateRequest.getThoughts());
        }

        // 3. 변경 내용 저장
        userChallengeRepository.save(userChallenge);

        // 4. ChallengeResponseDTO 반환
        return ChallengeConverter.toChallengeModifyProofDTO(userChallenge);
    }

}

