package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.Keyword;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeRequestDTO;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.util.ArrayList;
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
    public static ChallengeResponseDTO.SelectChallengeDTO toSelectChallengeDTO(List<UserChallenge> userChallenges) {
        List<ChallengeResponseDTO.SelectedChallengesInfo> selectedChallenges = userChallenges.stream()
                .map(userChallenge -> ChallengeResponseDTO.SelectedChallengesInfo.builder()
                        .id(userChallenge.getId()) // ✅ UserChallenge ID
                        .challengeId(userChallenge.getChallenge().getId()) // ✅ Challenge ID
                        .dtype(userChallenge.getDtype())
                        .title(userChallenge.getChallenge().getTitle())
                        .content(userChallenge.getChallenge().getContent())
                        .time(userChallenge.getChallenge().getTime())
                        .build())
                .toList();

        return ChallengeResponseDTO.SelectChallengeDTO.builder()
                .selectedChallenges(selectedChallenges) // ✅ 여러 개의 챌린지 리스트 반환
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

    // 챌린지 삭제
    public static ChallengeResponseDTO.DeleteChallengeResponseDTO toDeletedUserChallenge(UserChallenge userChallenge) {
        return ChallengeResponseDTO.DeleteChallengeResponseDTO.builder()
                .id(userChallenge.getId())
                .message("챌린지를 삭제했어요")
                .build();
    }

    // 챌린지 추천
    public static ChallengeResponseDTO.RecommendChallengesResponseDTO toRecommendedChallengeDTO(List<Keyword> analyzedEmotions, List<Challenge> dailyChallenges, Challenge randomChallenge) {
       // 감정키워드 변환
        List<KeywordResponseDTO.KeywordDTO> emotionKeywordsDTOs = KeywordConverter.toKeywordsDTO(analyzedEmotions);

        // daily 챌린지 변환
        List<ChallengeResponseDTO.ChallengeDTO> dailyChallengesDTOs = dailyChallenges.stream()
                .map(challenge ->
                        ChallengeResponseDTO.ChallengeDTO.builder()
                        .id(challenge.getId())
                        .title(challenge.getTitle())
                        .content(challenge.getContent())
                        .time(challenge.getTime())
                        .type(UserChallengeType.DAILY)
                        .build())
                .toList();

        // random 챌린지 변환
        ChallengeResponseDTO.ChallengeDTO randomChallengeDTO = ChallengeResponseDTO.ChallengeDTO.builder()
                .id(randomChallenge.getId())
                .title(randomChallenge.getTitle())
                .content(randomChallenge.getContent())
                .time(randomChallenge.getTime())
                .type(UserChallengeType.RANDOM) // RANDOM 타입 설정
                .build();

        // 최종 response
        return ChallengeResponseDTO.RecommendChallengesResponseDTO.builder()
                .emotionKeywords(emotionKeywordsDTOs)
                .dailyChallenges(dailyChallengesDTOs)
                .randomChallenge(randomChallengeDTO)
                .build();
    }
}
