package umc.GrowIT.Server.service.challengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ChallengeHandler;
import umc.GrowIT.Server.converter.ChallengeConverter;
import umc.GrowIT.Server.domain.Diary;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.repository.ChallengeRepository;
import umc.GrowIT.Server.repository.UserChallengeRepository;
import umc.GrowIT.Server.repository.DiaryRepository;
import umc.GrowIT.Server.service.keywordService.KeywordService;
import umc.GrowIT.Server.util.S3Util;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final DiaryRepository diaryRepository;
    private final KeywordService keywordService;
    private final S3Util s3Util;

    // 총 크레딧 수 조회
    @Override
    public int getTotalCredits(Long userId) {
        Integer credits = challengeRepository.getTotalCreditsByUserId(userId);
        return (credits != null) ? credits : 0;
    }

    // 작성한 총 일기 수 조회
    @Override
    public int getTotalDiaries(Long userId) {
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

    // 챌린지 홈 조회
    @Override
    @Transactional
    public ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId) {
        LocalDate today = LocalDate.now();

        // 오늘 날짜의 일기 존재 여부 확인
        Optional<Diary> todayDiary = diaryRepository.findTodayDiaryByUserId(userId, today);

        // 감정 키워드 조회
        List<String> keywordNames = todayDiary.isPresent()
                ? keywordService.getTodayDiaryKeywords(userId)
                : Collections.emptyList();

        // 사용자가 직접 저장한 챌린지만 가져옴
        List<UserChallenge> savedChallenges = todayDiary.isPresent()
                ? userChallengeRepository.findTodayUserChallengesByUserId(userId, today)
                : Collections.emptyList();

        return ChallengeConverter.toChallengeHomeDTO(
                savedChallenges,
                getTotalCredits(userId),
                getTotalDiaries(userId),
                getDiaryDate(userId),
                keywordNames
        );
    }

    // 챌린지 현황 조회
    @Override
    public ChallengeResponseDTO.ChallengeStatusPagedResponseDTO getChallengeStatus(Long userId, UserChallengeType challengeType, Boolean completed, Integer page) {
        PageRequest pr = PageRequest.of(page - 1, 5);
        Page<UserChallenge> userChallenges;

        if (challengeType == null) {
            // 전체 챌린지 중 완료/미완료 챌린지 조회
            userChallenges = userChallengeRepository.findChallengesByCompletionStatus(userId, completed, pr);
        } else {
            // 특정 챌린지 타입 + 완료/미완료 챌린지 조회
            userChallenges = userChallengeRepository.findByTypeAndCompletion(userId, challengeType, completed, pr);
        }

        return ChallengeConverter.toChallengeStatusPagedDTO(userChallenges);
    }

    // 챌린지 인증 내역 조회
    @Override
    @Transactional(readOnly = true)
    public ChallengeResponseDTO.ProofDetailsDTO getChallengeProofDetails(Long userId, Long userChallengeId) {

        UserChallenge userChallenge = userChallengeRepository.findByIdAndUserId(userChallengeId, userId)
                .orElseThrow(() -> new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_FOUND));

        if (!userChallenge.isCompleted()) {
            throw new ChallengeHandler(ErrorStatus.USER_CHALLENGE_NOT_PROVED);
        }

        String certificationImageUrl = s3Util.toGetPresignedUrl("challenges/" + userChallenge.getCertificationImageName(), Duration.ofMinutes(30));
        return ChallengeConverter.toProofDetailsDTO(userChallenge, certificationImageUrl);
    }

}
