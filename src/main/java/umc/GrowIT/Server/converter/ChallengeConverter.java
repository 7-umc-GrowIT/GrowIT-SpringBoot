package umc.GrowIT.Server.converter;

import org.springframework.data.domain.Page;
import umc.GrowIT.Server.domain.Challenge;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.UserChallenge;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.ChallengeDTO.ChallengeResponseDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ChallengeConverter {

    // 챌린지 1개를 ChallengeHomeDTO.RecommendedChallenge로 변환
    public static ChallengeResponseDTO.RecommendedChallengeDTO toRecommendedChallengeDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.RecommendedChallengeDTO.builder()
                .id(userChallenge.getId())
                .title(userChallenge.getChallenge().getTitle())
                .content(userChallenge.getChallenge().getContent())
                .challengeType(userChallenge.getChallengeType())
                .time(userChallenge.getChallenge().getTime())
                .completed(userChallenge.isCompleted())
                .build();
    }

    // 추천 챌린지 리스트를 반환
    public static List<ChallengeResponseDTO.RecommendedChallengeDTO> toRecommendedChallengeListDTO(
            List<UserChallenge> userChallenges) {

        return userChallenges.stream()
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
                .challengeType(userChallenge.getChallengeType())
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

    // 챌린지 인증 이미지 - 프리사인드 URL
    public static ChallengeResponseDTO.ProofPresignedUrlResponseDTO proofPresignedUrlDTO(String presignedUrl, String fileName) {
        return ChallengeResponseDTO.ProofPresignedUrlResponseDTO.builder()
                .presignedUrl(presignedUrl)
                .fileName(fileName)
                .build();
    }

    // 챌린지 인증 작성 결과
    public static ChallengeResponseDTO.CreateProofDTO toCreateProofDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.CreateProofDTO.builder()
                .id(userChallenge.getId())
                .title(userChallenge.getChallenge().getTitle())
                .certificationImageName(userChallenge.getCertificationImageName())
                .thoughts(userChallenge.getThoughts())
                .certificationDate(userChallenge.getCertificationDate())
                .build();
    }

    // 챌린지 인증 내역 조회
    public static ChallengeResponseDTO.ProofDetailsDTO toProofDetailsDTO(UserChallenge userChallenge, String certificationImageUrl) {
        return ChallengeResponseDTO.ProofDetailsDTO.builder()
                .id(userChallenge.getId())
                .title(userChallenge.getChallenge().getTitle())
                .time(userChallenge.getChallenge().getTime())
                .certificationImageUrl(certificationImageUrl)
                .thoughts(userChallenge.getThoughts())
                .certificationDate(userChallenge.getCertificationDate())
                .build();
    }

    // UserChallenge 생성
    public static UserChallenge createUserChallenge(User user, Challenge challenge, UserChallengeType challengeType, LocalDate date) {
        return UserChallenge.builder()
                .user(user)
                .challenge(challenge)
                .challengeType(challengeType)
                .date(date)
                .completed(false)
                .build();
    }

    // 선택한 챌린지 저장
    public static ChallengeResponseDTO.SelectChallengeResponseDTO toSelectChallengeDTO(List<UserChallenge> userChallenges) {
        List<ChallengeResponseDTO.SelectedChallengesInfo> selectedChallenges = userChallenges.stream()
                .map(userChallenge -> ChallengeResponseDTO.SelectedChallengesInfo.builder()
                        .id(userChallenge.getId())
                        .challengeType(userChallenge.getChallengeType())
                        .title(userChallenge.getChallenge().getTitle())
                        .content(userChallenge.getChallenge().getContent())
                        .time(userChallenge.getChallenge().getTime())
                        .build())
                .toList();

        return ChallengeResponseDTO.SelectChallengeResponseDTO.builder()
                .selectedChallenges(selectedChallenges) // 여러 개의 챌린지 리스트 반환
                .build();
    }

    // 챌린지 수정
    public static ChallengeResponseDTO.ModifyProofDTO toChallengeModifyProofDTO(UserChallenge userChallenge) {
        return ChallengeResponseDTO.ModifyProofDTO.builder()
                .certificationImageName(userChallenge.getCertificationImageName())
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
                        .challengeType(UserChallengeType.DAILY)
                        .build())
                .collect(Collectors.toList());

        // random 챌린지 변환 후 리스트에 추가
        recommendedChallenges.add(
                ChallengeResponseDTO.ChallengeDTO.builder()
                        .id(randomChallenge.getId())
                        .title(randomChallenge.getTitle())
                        .content(randomChallenge.getContent())
                        .time(randomChallenge.getTime())
                        .challengeType(UserChallengeType.RANDOM)
                        .build()
        );

        return recommendedChallenges;
    }
}
