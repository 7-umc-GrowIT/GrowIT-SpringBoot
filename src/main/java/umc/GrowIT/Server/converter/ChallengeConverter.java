package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.ChallengeKeyword;
import umc.GrowIT.Server.domain.Keyword;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeConverter {

    // 챌린지 1개를 ChallengeHomeDTO.RecommendedChallenge로 변환
    public static ChallengeResponseDTO.RecommendedChallengeDTO toRecommendedChallengeDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.RecommendedChallengeDTO.builder()
                .title(userChallenge.getChallenge().getTitle())
                .content(userChallenge.getChallenge().getContent())
                .dtype(userChallenge.getDtype())
                .time(userChallenge.getChallenge().getTime())
                .completed(userChallenge.isCompleted())
                .build();
    }

    // 추천 챌린지 리스트를 반환
    public static List<ChallengeResponseDTO.RecommendedChallengeDTO> toRecommendedChallengeListDTO(List<UserChallenge> userChallenges) {
        // 1. dtype이 "DAILY"인 챌린지 2개 선택
        List<UserChallenge> dailyChallenges = userChallenges.stream()
                .filter(userChallenge -> userChallenge.getDtype() == UserChallengeType.DAILY)
                .limit(2)
                .toList();

        // 2. dtype이 "RANDOM"인 챌린지 1개 선택
        List<UserChallenge> randomChallenge = userChallenges.stream()
                .filter(userChallenge -> userChallenge.getDtype() == UserChallengeType.RANDOM)
                .limit(1)
                .toList();

        // 3. 선택된 챌린지들을 합쳐서 DTO로 변환
        List<UserChallenge> selectedChallenges = new ArrayList<>();
        selectedChallenges.addAll(dailyChallenges);
        selectedChallenges.addAll(randomChallenge);

        return selectedChallenges.stream()
                .map(ChallengeConverter::toRecommendedChallengeDTO)
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
    public static ChallengeResponseDTO.ChallengeHomeDTO toChallengeHomeDTO(List<UserChallenge> userChallenges,
                                                                        int totalCredits,
                                                                        int totalDiaries,
                                                                        String diaryGoal, List<String> keywords) {

        return ChallengeResponseDTO.ChallengeHomeDTO.builder()
                .challengeKeywords(keywords) // 변환된 DTO 적용
                .recommendedChallenges(toRecommendedChallengeListDTO(userChallenges))
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
    public static ChallengeResponseDTO.ProofDetailsDTO toProofDetailsDTO(Challenge challenge, UserChallenge userChallenge, String imageUrl) {
        return ChallengeResponseDTO.ProofDetailsDTO.builder()
                .challengeId(challenge.getId())
                .title(challenge.getTitle())
                .time(challenge.getTime())
                .certificationImage(imageUrl)
                .thoughts(userChallenge.getThoughts())
                .certificationDate(userChallenge.getCreatedAt())
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
