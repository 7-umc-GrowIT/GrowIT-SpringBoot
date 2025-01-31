package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
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
    public static List<ChallengeResponseDTO.ChallengeStatusDTO> toChallengeStatusListDTO(List<UserChallenge> userChallenges) {
        return userChallenges.stream()
                .map(userChallenge -> ChallengeResponseDTO.ChallengeStatusDTO.builder()
                        .id(userChallenge.getChallenge().getId())
                        .title(userChallenge.getChallenge().getTitle())
                        .dtype(userChallenge.getDtype())
                        .time(userChallenge.getChallenge().getTime())
                        .completed(userChallenge.isCompleted())
                        .build())
                .collect(Collectors.toList());
    }

    // 챌린지 인증 작성 결과 반환
    public static ChallengeResponseDTO.ProofDetailsDTO toProofDetailsDTO(Challenge challenge, UserChallenge userChallenge) {
        return ChallengeResponseDTO.ProofDetailsDTO.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .time(challenge.getTime())
                .certificationImage(userChallenge.getCertificationImage())
                .thoughts(userChallenge.getThoughts())
                .certificationDate(userChallenge.getCreatedAt())
                .build();
    }

    // UserChallenge 생성
    public static UserChallenge createUserChallenge(User user, Challenge challenge, UserChallengeType dtype) {
        return UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .dtype(dtype)
                .completed(false)
                .build();
    }

    // 선택한 챌린지 저장
    public static ChallengeResponseDTO.SelectChallengeDTO toSelectChallengeDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.SelectChallengeDTO.builder()
                .id(userChallenge.getChallenge().getId())
                .dtype(userChallenge.getDtype())
                .title(userChallenge.getChallenge().getTitle())
                .content(userChallenge.getChallenge().getContent())
                .time(userChallenge.getChallenge().getTime())
                .build();
    }

    // 챌린지 수정
    public static ChallengeResponseDTO.ModifyProofDTO toChallengeModifyProofDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.ModifyProofDTO.builder()
                .certificationImage(userChallenge.getCertificationImage())
                .thoughts(userChallenge.getThoughts())
                .build();
    }

    // 챌린지 인증 내역 조회
    public static ChallengeResponseDTO.ProofDetailsDTO toChallengeProofDetailsDTO(Challenge challenge, UserChallenge userChallenge) {
        return ChallengeResponseDTO.ProofDetailsDTO.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .time(challenge.getTime())
                .certificationImage(userChallenge.getCertificationImage())
                .thoughts(userChallenge.getThoughts())
                .certificationDate(userChallenge.getCreatedAt())
                .build();
    }

    public static ChallengeResponseDTO.DeleteChallengeResponseDTO toDeletedUserChallenge(UserChallenge userChallenge) {
        return ChallengeResponseDTO.DeleteChallengeResponseDTO.builder()
                .id(userChallenge.getId())
                .message("챌린지를 삭제했어요")
                .build();
    }
}
