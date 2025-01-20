package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.ChallengeStatus;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ChallengeConverter {

    // 챌린지 1개를 ChallengeHomeDTO.RecommendedChallenge로 변환
    public static ChallengeResponseDTO.RecommendedChallengeDTO toRecommendedChallengeDTO(Challenge challenge, boolean isCompleted) {
        return ChallengeResponseDTO.RecommendedChallengeDTO.builder()
                .challengeKeywords(challenge.getChallengeKeywords())
                .title(challenge.getTitle())
                .time(challenge.getTime())
                .isCompleted(isCompleted)
                .build();
    }

    // 추천 챌린지 리스트를 반환
    public static List<ChallengeResponseDTO.RecommendedChallengeDTO> toRecommendedChallengeListDTO(List<Challenge> challenges, List<Long> completedChallengeIds) {
        return challenges.stream()
                .map(challenge -> toRecommendedChallengeDTO(challenge, completedChallengeIds.contains(challenge.getId())))
                .collect(Collectors.toList());
    }

    // 챌린지 리포트를 ChallengeHome.ChallengeReport로 변환
    public static ChallengeResponseDTO.ChallengeReportDTO toChallengeReportDTO(int totalCredits, int totalDiaries, String userDate) {
        return ChallengeResponseDTO.ChallengeReportDTO.builder()
                .totalCredits(totalCredits)
                .totalDiaries(totalDiaries)
                .userDate(userDate)
                .build();
    }

    // 전체 챌린지 홈 데이터를 ChallengeHomeDTO로 변환
    public static ChallengeResponseDTO.ChallengeHomeDTO toChallengeHomeDTO(List<Challenge> recommendedChallenges,
                                                                        List<Long> completedChallengeIds,
                                                                        int totalCredits,
                                                                        int totalDiaries,
                                                                        String diaryGoal) {
        return ChallengeResponseDTO.ChallengeHomeDTO.builder()
                .recommendedChallenges(toRecommendedChallengeListDTO(recommendedChallenges, completedChallengeIds))
                .challengeReport(toChallengeReportDTO(totalCredits, totalDiaries, diaryGoal))
                .build();
    }

    // 챌린지 현황
    public static List<ChallengeResponseDTO.ChallengeStatusDTO> toChallengeStatusListDTO(List<Challenge> challenges) {
        return challenges.stream()
                .map(challenge -> ChallengeResponseDTO.ChallengeStatusDTO.builder()
                        .id(challenge.getId())
                        .title(challenge.getTitle())
                        .time(challenge.getTime())
                        .status(challenge.getStatus()) // 상태
                        .completed(challenge.isCompleted()) // 완료 여부
                        .build())
                .collect(Collectors.toList());
    }


    public static ChallengeResponseDTO.DeleteChallengeResponseDTO toDeletedUserChallenge(UserChallenge userChallenge) {
        return ChallengeResponseDTO.DeleteChallengeResponseDTO.builder()
                .id(userChallenge.getId())
                .message("챌린지를 삭제했어요")
                .build();
    }
}
