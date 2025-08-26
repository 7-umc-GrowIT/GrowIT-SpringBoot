package umc.GrowIT.Server.web.dto.ChallengeDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.time.LocalDateTime;
import java.util.List;

public class ChallengeResponseDTO {
    // 챌린지 홈 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeHomeDTO {
        @Schema(description = "챌린지 키워드(리스트)", example = "즐거운, 차분한, 새로운")
        private List<String> challengeKeywords;
        @Schema(description = "오늘의 추천 챌린지(리스트)", example = "좋아하는 책 독서하기")
        private List<RecommendedChallengeDTO> recommendedChallenges;
        @Schema(description = "챌린지 리포트")
        private ChallengeReportDTO challengeReport;
    }

    // 챌린지 추천
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendedChallengeDTO {
        @Schema(description = "추천 챌린지 id", example = "1")
        private Long id;
        @Schema(description = "추천 챌린지 제목", example = "좋아하는 책 독서하기")
        private String title;
        @Schema(description = "추천 챌린지 내용", example = "좋아하는 책을 골라서 한 번 읽어 보세요!")
        private String content;
        @Schema(description = "챌린지 타입(DAILY or RANDOM)", example = "DAILY")
        private UserChallengeType dtype;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
        @Schema(description = "챌린지 완료(인증) 여부", example = "false")
        private boolean completed;
    }

    // 챌린지 리포트
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeReportDTO {
        @Schema(description = "총 크레딧 수", example = "1200")
        private int totalCredits;
        @Schema(description = "작성한 일기 수", example = "16")
        private int totalDiaries;
        @Schema(description = "일기를 작성해온 기간", example = "D+15")
        private String diaryDate;
    }

    // 챌린지 상태별 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeStatusDTO {
        @Schema(description = "챌린지 id", example = "1")
        private Long id;
        @Schema(description = "챌린지 제목", example = "좋아하는 책 독서하기")
        private String title;
        @Schema(description = "챌린지 타입(DAILY or RANDOM)", example = "DAILY")
        private UserChallengeType dtype;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
        @Schema(description = "챌린지 완료(인증) 여부", example = "false")
        private boolean completed;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChallengeStatusPagedResponseDTO  {
        @Schema(description = "챌린지 목록")
        private List<ChallengeStatusDTO> content;
        @Schema(description = "현재 페이지", example = "1")
        private int currentPage;
        @Schema(description = "첫번째 페이지인지", example = "true")
        private boolean isFirst;
        @Schema(description = "마지막 페이지인지", example = "true")
        private boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofPresignedUrlResponseDTO {
        @Schema(description = "S3 업로드용 Presigned URL", example = "https://growit-server-bucket.s3.ap-northeast-2.amazonaws.com/challenges/1842f2aa-40d0-4ae3~")
        private String presignedUrl;
        @Schema(description = "업로드 완료 후 접근 URL", example = "3c99605a8e01.png")
        private String fileUrl;
    }

    // 챌린지 인증 내역
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofDetailsDTO {
        @Schema(description = "챌린지 인증 내역 id", example = "1")
        private Long id;
        @Schema(description = "챌린지 제목", example = "학교 도서관에서 독서하기")
        private String title;
        @Schema(description = "챌린지 인증 이미지", example = "https://growit-server-bucket.s3.ap-northeast-2.amazonaws.com/challenges/1842f2aa-40d0-4ae3~")
        private String certificationImage;
        @Schema(description = "챌린지 인증 소감", example = "오늘은 우주공강에 학교 도서관에 가서~")
        private String thoughts;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
        @Schema(description = "챌린지 인증 날짜", example ="2025-08-26T01:11:50")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime certificationDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectChallengeDTO {
        @Schema(description = "선택하여 저장한 챌린지(리스트)")
        private List<SelectedChallengesInfo> selectedChallenges;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectedChallengesInfo {
        @Schema(description = "챌린지 id", example = "1")
        private Long id;
        @Schema(description = "챌린지 타입(DAILY or RANDOM)", example = "DAILY")
        private UserChallengeType dtype;
        @Schema(description = "챌린지 제목", example = "좋아하는 책 독서하기")
        private String title;
        @Schema(description = "챌린지 내용", example = "좋아하는 책을 골라서 한 번 읽어 보세요!")
        private String content;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
    }

    // 챌린지 수정
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyProofDTO {
        @Schema(description = "챌린지 인증 이미지", example = "3c99605a8e01.png")
        private String certificationImage;
        @Schema(description = "챌린지 인증 소감", example = "오늘은 우주공강에 학교 도서관에 가서~")
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
