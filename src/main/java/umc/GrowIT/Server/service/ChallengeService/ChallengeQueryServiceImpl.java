package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.ChallengeHandler;
import umc.GrowIT.Server.converter.ChallengeConverter;
import umc.GrowIT.Server.domain.Keyword;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.repository.ChallengeKeywordRepository;
import umc.GrowIT.Server.repository.ChallengeRepository.ChallengeRepository;
import umc.GrowIT.Server.repository.KeywordRepository;
import umc.GrowIT.Server.repository.ChallengeRepository.UserChallengeRepository;
import umc.GrowIT.Server.repository.diaryRepository.DiaryRepository;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final KeywordRepository keywordRepository;
    private final ChallengeKeywordRepository challengeKeywordRepository;
    private final DiaryRepository diaryRepository;
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
        // TODO: 사용자가 일기를 처음 작성한 날짜와 오늘 날짜 간의 차이를 계산
        LocalDate firstDiaryDate = diaryRepository.findFirstDiaryDateByUserId(userId) // 최초 일기 작성 날짜 조회
                .orElse(LocalDate.now()); // 일기를 작성한 기록이 없으면 오늘 날짜를 기본값으로 사용

        // 오늘 날짜와의 차이를 계산
        long days = ChronoUnit.DAYS.between(firstDiaryDate, LocalDate.now());

        return "D+" + days;
    }


    @Override
    public ChallengeResponseDTO.ChallengeHomeDTO getChallengeHome(Long userId) {
        // 1. 오늘 저장된 챌린지 조회
        List<UserChallenge> todayChallenges = userChallengeRepository.findTodayUserChallengesByUserId(userId);

        // 2. Keyword 데이터 조회 후 문자열 리스트로 변환
        List<String> keywordNames = keywordRepository.findAll()
                .stream()
                .limit(3) // 최대 3개만 추출
                .map(Keyword::getName)
                .toList();

        // 3. DTO 생성 및 반환
        return ChallengeConverter.toChallengeHomeDTO(
                todayChallenges, // 오늘 저장된 챌린지만 전달
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
            throw new ChallengeHandler(ErrorStatus.CHALLENGE_VERIFY_NOT_EXISTS);
        }

        String imageUrl = userChallenge.getCertificationImage();
        return ChallengeConverter.toProofDetailsDTO(userChallenge.getChallenge(), userChallenge, imageUrl);
    }


}
