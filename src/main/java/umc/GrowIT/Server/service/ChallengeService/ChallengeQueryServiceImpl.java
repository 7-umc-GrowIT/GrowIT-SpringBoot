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
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.repository.ChallengeKeywordRepository;
import umc.GrowIT.Server.repository.ChallengeRepository;
import umc.GrowIT.Server.repository.KeywordRepository;
import umc.GrowIT.Server.repository.UserChallengeRepository;
import umc.GrowIT.Server.service.S3Service.S3Service;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.service.KeywordService.KeywordService;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final S3Service s3Service;
    private final DiaryRepository diaryRepository;
    private final KeywordService keywordService;


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
    public String getDiaryDate(Long userId) {
        // 사용자가 마지막으로 일기를 작성한 날짜 조회
        LocalDate lastDiaryDate = diaryRepository.findLastDiaryDateByUserId(userId)
                .orElse(LocalDate.now()); // 작성 기록이 없으면 오늘 날짜를 기본값으로 사용

        // 오늘 날짜와의 차이를 계산
        long days = ChronoUnit.DAYS.between(lastDiaryDate, LocalDate.now());

        return "D+" + days;
    }


    @Override
    @Transactional
    public ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId) {

        // 오늘의 시작 시간과 끝 시간 계산
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay(); // 오늘 자정 00:00:00
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX); // 오늘 끝 23:59:59

        // 오늘 저장된 챌린지 조회
        List<UserChallenge> todayChallenges = userChallengeRepository.findTodayUserChallengesByUserId(userId, startOfDay, endOfDay);

        // 감정 키워드 조회 (사용자가 오늘 작성한 일기 기반)
        List<String> keywordNames = keywordService.getTodayDiaryKeywords(userId);

        return ChallengeConverter.toChallengeHomeDTO(
                todayChallenges,
                getTotalCredits(userId),
                getTotalDiaries(userId),
                getDiaryDate(userId),
                keywordNames
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
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_NOT_COMPLETED);
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
