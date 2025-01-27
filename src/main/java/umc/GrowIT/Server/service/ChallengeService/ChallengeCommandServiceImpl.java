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

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    @Transactional
    public ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long challengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {

        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        if (userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_ALREADY_EXISTS);
        }
        if (proofRequest != null) {
            userChallenge.verifyUserChallenge(proofRequest);
        }

        userChallengeRepository.save(userChallenge);
        return ChallengeConverter.toProofDetailsDTO(userChallenge.getChallenge(), userChallenge);
    }


    // 챌린지 인증 내역 조회
    @Override
    @Transactional(readOnly = true)
    public ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long userId, Long challengeId) {

        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(challengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_NOT_EXISTS));
        Challenge challenge = userChallenge.getChallenge();
        return ChallengeConverter.toChallengeProofDetailsDTO(challenge, userChallenge);
    }

    @Override
    @Transactional
    public ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long challengeId, ChallengeRequestDTO.UpdateRequestDTO updateRequest) {
        // 1. 해당 챌린지 조회
        UserChallenge userChallenge = challengeRepository.findByChallengeId(challengeId)
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


    public ChallengeResponseDTO.DeleteChallengeResponseDTO delete(Long userChallengeId, Long userId) {
        // 1. userId를 조회하고 없으면 오류
        userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 2. userChallengeId와 userId를 통해 조회하고 없으면 오류
        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        // 3. 진행 중(false)인 챌린지인지 체크, 완료(true)한 챌린지면 오류
        if(userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.USER_CHALLENGE_COMPLETE);
        }

        // 4. 삭제
        userChallengeRepository.deleteById(userChallengeId);

        // 5. converter 작업
        return ChallengeConverter.toDeletedUserChallenge(userChallenge);
    }
}
