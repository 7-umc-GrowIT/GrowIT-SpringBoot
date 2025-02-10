package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.Keyword;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeConverter {

    // 챌린지 1개를 ChallengeHomeDTO.RecommendedChallenge로 변환
    public static ChallengeResponseDTO.RecommendedChallengeDTO toRecommendedChallengeDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.RecommendedChallengeDTO.builder()
                .id(userChallenge.getId())
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
    public static ChallengeResponseDTO.ChallengeReportDTO toChallengeReportDTO(int totalCredits, int totalDiaries, String diaryDate) {
        return ChallengeResponseDTO.ChallengeReportDTO.builder()
                .totalCredits(totalCredits)
                .totalDiaries(totalDiaries)
                .diaryDate(diaryDate)
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
                        .id(userChallenge.getId())
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
                .id(userChallenge.getId())
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
                        .id(userChallenge.getId())
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
                .id(userChallenge.getId())
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
        List<ChallengeResponseDTO.ChallengeDTO> recommendedChallenges = dailyChallenges.stream()
                .map(challenge -> ChallengeResponseDTO.ChallengeDTO.builder()
                        .id(challenge.getId())
                        .title(challenge.getTitle())
                        .content(challenge.getContent())
                        .time(challenge.getTime())
                        .type(UserChallengeType.DAILY)
                        .build())
                .collect(Collectors.toList());

        // random 챌린지 변환 후 리스트에 추가
        recommendedChallenges.add(
                ChallengeResponseDTO.ChallengeDTO.builder()
                        .id(randomChallenge.getId())
                        .title(randomChallenge.getTitle())
                        .content(randomChallenge.getContent())
                        .time(randomChallenge.getTime())
                        .type(UserChallengeType.RANDOM)
                        .build()
        );

        // 최종 response
        return ChallengeResponseDTO.RecommendChallengesResponseDTO.builder()
                .emotionKeywords(emotionKeywordsDTOs)
                .recommendedChallenges(recommendedChallenges)
                .build();
    }
}
