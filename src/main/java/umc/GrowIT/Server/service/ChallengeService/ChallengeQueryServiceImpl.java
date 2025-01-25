package umc.GrowIT.Server.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.GrowIT.Server.converter.ChallengeConverter;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.ChallengeType;
import umc.GrowIT.Server.repository.ChallengeRepository;
import umc.GrowIT.Server.repository.UserChallengeRepository;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;

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
    public ChallengeResponseDTO.ChallengeStatusListDTO getChallengeStatus(Long userId, ChallengeType dtype, Boolean completed) {
        List<ChallengeResponseDTO.ChallengeStatusDTO> challenges;
        if (completed) {
            // 특정 유저의 인증 완료된 챌린지 조회
            List<UserChallenge> userChallenges = userChallengeRepository.findUserChallengesByStatusAndCompletion(userId, dtype);
            challenges = ChallengeConverter.toChallengeStatusListDTO(userChallenges);
        } else {
            // 특정 유저가 완료하지 않은 챌린지 조회
            List<Challenge> availableChallenges = userChallengeRepository.findAvailableChallengesForUser(userId, dtype);
            List<UserChallenge> userChallenges = userChallengeRepository.findUserChallengesByUserId(userId); // 유저의 모든 UserChallenge 조회
            challenges = ChallengeConverter.toChallengeStatusListDTOFromChallenge(availableChallenges, userChallenges);
        }

        return ChallengeResponseDTO.ChallengeStatusListDTO.builder()
                .challenges(challenges)
                .build();
    }



}
