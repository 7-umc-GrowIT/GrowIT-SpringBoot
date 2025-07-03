package umc.GrowIT.Server.web.dto.ChallengeDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.enums.UserChallengeType;
import umc.GrowIT.Server.web.dto.KeywordDTO.KeywordResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeResponseDTO {
    // 챌린지 홈 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeHomeDTO {
        private List<String> challengeKeywords;
        private List<RecommendedChallengeDTO> recommendedChallenges; // 오늘의 챌린지 추천
        private ChallengeReportDTO challengeReport; // 챌린지 리포트
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeywordDTO {
        private String name;
    }
    // 챌린지 추천
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedChallengeDTO {
        private Long id;
        private String title;
        private String content;
        private UserChallengeType dtype;
        private Integer time;
        private boolean completed;
    }

    // 챌린지 리포트
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeReportDTO {
        private int totalCredits;
        private int totalDiaries;
        private String diaryDate;
    }

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
    public static class ChallengeStatusDTO {
        private Long id;
        private String title;
        private UserChallengeType dtype;
        private Integer time;
        private boolean completed;
    }

    // 페이징 적용
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeStatusPagedResponseDTO  {
        private List<ChallengeStatusDTO> content;
        private int currentPage;
        private boolean isFirst;
        private boolean isLast;
    }

    // 챌린지 인증 내역
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofDetailsDTO {
        private Long id;
        private String title;
        private String certificationImageUrl;
        private String thoughts;
        private Integer time;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime certificationDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectChallengeDTO {
        private List<SelectedChallengesInfo> selectedChallenges;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedChallengesInfo {
        private Long id;
        private UserChallengeType dtype;
        private String title;
        private String content;
        private Integer time;
    }

    // 챌린지 수정
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyProofDTO {
        private String certificationImageUrl;
        private String thoughts;
    }

    // 챌린지 삭제 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteChallengeResponseDTO {
        // TODO 디테일하게 결정 필요
        private Long id;
        private String message; // ex) 챌린지 삭제가 완료되었습니다
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeDTO {
        private Long id; // 챌린지 ID
        private String title; //챌린지 제목
        private String content; //챌린지 내용
        private Integer time; //챌린지 소요시간
        private UserChallengeType type; //추천 타입 (Daily or Random)
    }
}
