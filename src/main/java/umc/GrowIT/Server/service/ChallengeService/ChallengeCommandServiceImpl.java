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
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.repository.ChallengeRepository.ChallengeRepository;
import umc.GrowIT.Server.repository.ChallengeRepository.UserChallengeRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.service.ImageService.ImageService;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final ImageService imageService;

    @Override
    public ChallengeResponseDTO.SelectChallengeDTO selectChallenge(Long userId, Long challengeId, UserChallengeType dtype) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_NOT_FOUND));

        UserChallenge userChallenge = ChallengeConverter.createUserChallenge(user, challenge, dtype); // userChallenge 생성
        userChallengeRepository.save(userChallenge);

        return ChallengeConverter.toSelectChallengeDTO(userChallenge);
    }



    @Override
    @Transactional
    public ChallengeResponseDTO.ProofDetailsDTO createChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {

        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        if (userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_ALREADY_EXISTS);
        }

        // 이미지 업로드
        String imageUrl = null;
        if (proofRequest.getCertificationImage() != null && !proofRequest.getCertificationImage().isEmpty()) {
            imageUrl = imageService.upload(proofRequest.getCertificationImage());
        }

        userChallenge.verifyUserChallenge(proofRequest, imageUrl);
        userChallengeRepository.save(userChallenge);
        return ChallengeConverter.toProofDetailsDTO(userChallenge.getChallenge(), userChallenge, imageUrl);
    }

    @Override
    @Transactional
    public ChallengeResponseDTO.ModifyProofDTO updateChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest) {
        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_NOT_EXISTS));

        String oldImageUrl = userChallenge.getCertificationImage();
        String newImageUrl = oldImageUrl;

        // 새 이미지 업로드 (새 이미지가 있을 경우에만)
        if (updateRequest.getCertificationImage() != null && !updateRequest.getCertificationImage().isEmpty()) {
            if (oldImageUrl != null) {
                imageService.delete(oldImageUrl); // 기존 이미지 삭제
            }
            newImageUrl = imageService.upload(updateRequest.getCertificationImage());
            userChallenge.setCertificationImage(newImageUrl); // 이미지 URL 업데이트
        }

        // 소감 업데이트 (null이 아닌 경우에만 변경)
        if(updateRequest.getThoughts() != null) {
            userChallenge.setThoughts(updateRequest.getThoughts());
        }

        userChallengeRepository.save(userChallenge);
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