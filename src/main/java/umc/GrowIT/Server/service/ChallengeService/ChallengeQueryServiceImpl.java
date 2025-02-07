package umc.GrowIT.Server.service.ChallengeService;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ChallengeHandler;
import umc.GrowIT.Server.converter.ChallengeConverter;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.repository.ChallengeRepository;
import umc.GrowIT.Server.repository.UserChallengeRepository;
import umc.GrowIT.Server.service.S3Service.S3Service;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final AmazonS3 amazonS3;
    private final S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public int getTotalCredits(Long userId) {
        // TODO: 총 크레딧 수 조회 로직 구현
        Integer credits = challengeRepository.getTotalCreditsByUserId(userId);
        return (credits != null) ? credits : 0;
    }

    @Override
    public int getTotalDiaries(Long userId) {
        // TODO: 작성된 일기 수 조회 로직 구현
        return challengeRepository.getTotalDiariesByUserId(userId);
    }

    @Override
    public String getUserDate(Long userId) {
        // TODO: 사용자가 가입한 날짜와 오늘 날짜 간의 차이를 계산
        LocalDate joinDate = challengeRepository.findJoinDateByUserId(userId)
                .orElse(LocalDate.now()); // 가입 날짜가 없으면 오늘 날짜를 기본값으로 사용

        // 오늘 날짜와의 차이를 계산
        long days = ChronoUnit.DAYS.between(joinDate, LocalDate.now());

        return "D+" + days;
    }

    @Override
    public ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId) {
        return ChallengeConverter.toChallengeHomeDTO(
                challengeRepository.findRecommendedChallengesByUserId(userId),
                challengeRepository.findCompletedChallengeIdsByUserId(userId),
                getTotalCredits(userId),
                getTotalDiaries(userId),
                getUserDate(userId)
        );
    }

    @Override
    public ChallengeResponseDTO.ChallengeStatusListDTO getChallengeStatus(Long userId, UserChallengeType dtype, Boolean completed) {
        List<UserChallenge> userChallenges;

        // dtype이 null이면 전체 챌린지 중 완료/미완료만 조회
        if (dtype == null) {
            userChallenges = userChallengeRepository.findChallengesByCompletionStatus(userId, completed);
        }
        // dtype이 RANDOM 또는 DAILY인 경우 미완료 챌린지만 조회 (completed = false 고정)
        else if (!completed) {
            userChallenges = userChallengeRepository.findChallengesByDtypeAndCompletionStatus(userId, dtype);
        }
        // dtype이 RANDOM 또는 DAILY인데 completed가 true이면 빈 리스트 반환 (잘못된 요청 방지)
        else {
            userChallenges = Collections.emptyList();
        }

        List<ChallengeResponseDTO.ChallengeStatusDTO> challenges = ChallengeConverter.toChallengeStatusListDTO(userChallenges);

        return ChallengeResponseDTO.ChallengeStatusListDTO.builder()
                .userChallenges(challenges)
                .build();
    }

    // 챌린지 인증 내역 조회
    @Override
    @Transactional(readOnly = true)
    public ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long userId, Long userChallengeId) {

        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        if (!userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_NOT_EXISTS);
        }

        // S3의 인증 이미지 경로 가져오기
        String imageKey = userChallenge.getCertificationImage();

        // Presigned URL 생성 (이미지가 존재하는 경우에만)
        String presignedUrl = (imageKey != null && !imageKey.isEmpty())
                ? s3Service.generatePresignedUrlForDownload(imageKey)
                : null; // 이미지가 없으면 null 반환

        return ChallengeConverter.toProofDetailsDTO(userChallenge.getChallenge(), userChallenge, presignedUrl);
    }

}
