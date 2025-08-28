package umc.GrowIT.Server.service.challengeService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.*;
import umc.GrowIT.Server.converter.ChallengeConverter;
import umc.GrowIT.Server.domain.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.repository.*;
import umc.GrowIT.Server.util.S3Util;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final UserRepository userRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final ChallengeRepository challengeRepository;
    private final S3Util s3Util;

    @Override
    @Transactional
    public void selectChallenges(Long userId, List<ChallengeRequestDTO.SelectChallengeRequestDTO> selectRequestList) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_NOT_FOUND));

        // 전체 선택된 챌린지 개수 초기화
        int dailyChallengeCount = 0;
        int randomChallengeCount = 0;

        for (ChallengeRequestDTO.SelectChallengeRequestDTO selectRequest : selectRequestList) {
            List<Long> challengeIds = selectRequest.getChallengeIds();
            UserChallengeType challengeType = selectRequest.getChallengeType();
            LocalDate date = selectRequest.getDate();

            // 현재 challengeType에 따라 저장 가능한 최대 개수 확인
            if (challengeType == UserChallengeType.DAILY) {
                dailyChallengeCount += challengeIds.size();
                if (dailyChallengeCount > 2) {
                    throw new ChallengeHandler(ErrorStatus.CHALLENGE_DAILY_MAX);
                }
            } else if (challengeType == UserChallengeType.RANDOM) {
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
                        return ChallengeConverter.createUserChallenge(user, challenge, challengeType, date);
                    })
                    .toList();

            userChallengeRepository.saveAll(userChallenges);
        }
    }

    // 챌린지 인증 이미지 업로드용 Presigned URL 생성
    @Override
    @Transactional
    public ChallengeResponseDTO.ProofPresignedUrlResponseDTO createChallengePresignedUrl(Long userId, ChallengeRequestDTO.ProofRequestPresignedUrlDTO request) {

        // contentType 검증
        String contentType = request.getContentType().toLowerCase();

        // 허용하는 확장자 매핑
        String ext = switch (contentType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/svg" -> "svg";
            case "image/webp" -> "webp";
            default -> throw new S3Handler(ErrorStatus.S3_BAD_FILE_EXTENSION);
        };

        String fileName = UUID.randomUUID() + "." + ext;
        String key = "challenges/" + fileName;
        String presignedUrl = s3Util.toCreatePresignedUrl(key, contentType);

        return ChallengeConverter.proofPresignedUrlDTO(presignedUrl, fileName);
    }

    @Override
    @Transactional
    public void createChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO proofRequest) {

        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        if (userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.USER_CHALLENGE_ALREADY_PROVED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        userChallenge.verifyUserChallenge(proofRequest);

        Integer challengeCredit = 1;
        user.updateCurrentCredit(user.getCurrentCredit() + challengeCredit);
        user.updateTotalCredit(user.getTotalCredit() + challengeCredit);
    }

    @Override
    @Transactional
    public void updateChallengeProof(Long userId, Long userChallengeId, ChallengeRequestDTO.ProofRequestDTO updateRequest) {
        // 유저 챌린지 조회
        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        // 인증이 완료되지 않았을 경우 예외 발생
        if (!userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_PROVED);
        }

        // 수정사항 체크
        boolean sameImage = Objects.equals(updateRequest.getCertificationImageName(), userChallenge.getCertificationImageName());
        boolean sameThoughts = Objects.equals(updateRequest.getThoughts(), userChallenge.getThoughts());

        // 인증 내역에 수정사항 없는 경우 에러 처리
        if (sameImage && sameThoughts) {
            throw new ChallengeHandler(ErrorStatus.USER_CHALLENGE_UPDATE_NO_CHANGES);
        }

        // 인증 이미지 + 소감 업데이트
        userChallenge.updateProof(updateRequest);
    }

    // 삭제
    @Override
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