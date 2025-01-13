package umc.GrowIT.Server.web.dto.ChallengeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeResponseDTO {
    // 챌린지 현황 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeHome {
        private int totalChallenges;
        private int completedChallenges;
        private List<ChallengeSummary> recentChallenges;
    }

    // 챌린지 홈 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeSummary {
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
    public static class ChallengeStatusList {
        private List<ChallengeStatus> challenges;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeStatus {
        private Long id;
        private String title;
        private String status;
        private boolean completed;
    }

    // 챌린지 인증 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofDetails {
        private Long challengeId;
        private String certificationImage;
        private String thoughts;
        private LocalDateTime createdAt;
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
