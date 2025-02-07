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
    @Transactional
    public ChallengeResponseDTO.SelectChallengeDTO selectChallenges(Long userId, List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_NOT_FOUND));

        // 전체 선택된 챌린지 개수 초기화
        int dailyChallengeCount = 0;
        int randomChallengeCount = 0;

        // 선택한 UserChallenge 저장
        List<UserChallenge> savedUserChallenges = new ArrayList<>();

        for (ChallengeRequestDTO.SelectChallengeRequestDTO selectRequest : selectRequestList) {
            List<Long> challengeIds = selectRequest.getChallengeIds();
            UserChallengeType dtype = selectRequest.getDtype();

            // 현재 dtype에 따라 저장 가능한 최대 개수 확인
            if (dtype == UserChallengeType.DAILY) {
                dailyChallengeCount += challengeIds.size();
                if (dailyChallengeCount > 2) {
                    throw new ChallengeHandler(ErrorStatus.CHALLENGE_DAILY_MAX);
                }
            } else if (dtype == UserChallengeType.RANDOM) {
                randomChallengeCount += challengeIds.size();
                if (randomChallengeCount > 1) {
                    throw new ChallengeHandler(ErrorStatus.CHALLENGE_RANDOM_MAX);
                }
            }

            // 데일리 챌린지와 랜덤 챌린지 합쳐서 최소 1개 이상 선택
            if (dailyChallengeCount == 0 && randomChallengeCount == 0) {
                throw new ChallengeHandler(ErrorStatus.CHALLENGE_AT_LEAST);
            }

            // 각 챌린지를 UserChallenge로 생성 및 저장
            List<UserChallenge> userChallenges = challengeIds.stream()
                    .map(challengeId -> {
                        Challenge challenge = challengeRepository.findById(challengeId)
                                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_FOUND));

                        // UserChallenge 생성
                        UserChallenge userChallenge = ChallengeConverter.createUserChallenge(user, challenge, dtype);
                        return userChallengeRepository.save(userChallenge);
                    })
                    .toList();

            savedUserChallenges.addAll(userChallenges);
        }


        // DTO 변환 및 반환
        return ChallengeConverter.toSelectChallengeDTO(savedUserChallenges);
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
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        // 인증이 완료되지 않았을 경우 예외 발생
        if (!userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_COMPLETED);
        }

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