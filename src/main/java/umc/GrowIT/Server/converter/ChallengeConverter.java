package umc.GrowIT.Server.converter;

import org.springframework.data.domain.Page;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.Keyword;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.time.LocalDate;
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
    public static List<ChallengeResponseDTO.RecommendedChallengeDTO> toRecommendedChallengeListDTO(
            List<UserChallenge> userChallenges) {

        LocalDate today = LocalDate.now();

        return userChallenges.stream()
                .filter(userChallenge -> userChallenge.getDate().isEqual(today))
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
    public static ChallengeResponseDTO.ChallengeStatusDTO toChallengeStatusDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.ChallengeStatusDTO.builder()
                .id(userChallenge.getId())
                .title(userChallenge.getChallenge().getTitle())
                .dtype(userChallenge.getDtype())
                .time(userChallenge.getChallenge().getTime())
                .completed(userChallenge.isCompleted())
                .build();
    }

    public static ChallengeResponseDTO.ChallengeStatusPagedResponseDTO toChallengeStatusPagedDTO(Page<UserChallenge> userChallenges) {
        Page<ChallengeResponseDTO.ChallengeStatusDTO> mappedPage = userChallenges.map(ChallengeConverter::toChallengeStatusDTO);

        return ChallengeResponseDTO.ChallengeStatusPagedResponseDTO.builder()
                .content(mappedPage.getContent())
                .currentPage(mappedPage.getNumber() + 1)
                .totalPages(mappedPage.getTotalPages())
                .totalElements(mappedPage.getTotalElements())
                .isFirst(mappedPage.isFirst())
                .isLast(mappedPage.isLast())
                .build();
    }

    // 챌린지 인증 작성 결과 반환
    public static ChallengeResponseDTO.ProofDetailsDTO toProofDetailsDTO(Challenge challenge, UserChallenge userChallenge) {
        return ChallengeResponseDTO.ProofDetailsDTO.builder()
                .id(userChallenge.getId())
                .title(challenge.getTitle())
                .time(challenge.getTime())
                .certificationImageUrl(userChallenge.getCertificationImageUrl())
                .thoughts(userChallenge.getThoughts())
                .certificationDate(userChallenge.getCertificationDate())
                .build();
    }

    // UserChallenge 생성
    public static UserChallenge createUserChallenge(User user, Challenge challenge, UserChallengeType dtype, LocalDate date) {
        return UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .dtype(dtype)
                .date(date)
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
                .selectedChallenges(selectedChallenges) // 여러 개의 챌린지 리스트 반환
                .build();
    }


    // 챌린지 수정
    public static ChallengeResponseDTO.ModifyProofDTO toChallengeModifyProofDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.ModifyProofDTO.builder()
                .certificationImageUrl(userChallenge.getCertificationImageUrl())
                .thoughts(userChallenge.getThoughts())
                .build();
    }

    // 챌린지 삭제
    public static ChallengeResponseDTO.DeleteChallengeResponseDTO toDeletedUserChallenge(UserChallenge userChallenge) {
        return ChallengeResponseDTO.DeleteChallengeResponseDTO.builder()
                .id(userChallenge.getId())
                .message("챌린지를 삭제했어요")
                .build();
    }

    public static List<ChallengeResponseDTO.ChallengeDTO> toRecommendedChallenges(List<Challenge> dailyChallenges, Challenge randomChallenge) {
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

        return recommendedChallenges;
    }
}
