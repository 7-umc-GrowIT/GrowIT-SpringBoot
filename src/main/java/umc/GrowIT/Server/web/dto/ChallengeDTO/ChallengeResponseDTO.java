package umc.GrowIT.Server.web.dto.ChallengeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.ChallengeKeyword;

import java.util.List;

public class ChallengeResponseDTO {
    // 챌린지 홈 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeHomeDTO {
        private List<RecommendedChallengeDTO> recommendedChallenges; // 오늘의 챌린지 추천
        private ChallengeReportDTO challengeReport; // 챌린지 리포트
    }

    // 챌린지 추천
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedChallengeDTO {
        private List<ChallengeKeyword> challengeKeywords;
        private String title;
        private Integer time;
        private boolean isCompleted;
    }

    // 챌린지 리포트
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeReportDTO {
        private int totalCredits;
        private int totalDiaries;
        private String userDate;
    }

    // 챌린지 현황 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeSummaryDTO {
        private Long id;
        private String title;
        private String status;
        private boolean completed;
    }

    // 챌린지 상태별 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeStatusListDTO {
        private List<ChallengeResponseDTO.ChallengeStatusDTO> challenges;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeStatusDTO {
        private Long id;
        private String title;
        private String status;
        private Integer time;
        private boolean completed;
    }

    // 챌린지 인증 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofDetailsDTO {
        private Long challengeId;
        private String certificationImage;
        private String thoughts;
        private boolean completed;
    }

    // 챌린지 삭제 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteChallengeResponseDTO {
        // TODO 디테일하게 결정 필요
        private String message; // ex) 챌린지 삭제가 완료되었습니다
    }
}
