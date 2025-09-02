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
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 홈 response")
    public static class ChallengeHomeDTO {
        @Schema(description = "챌린지 키워드(리스트)", example = "즐거운, 차분한, 새로운")
        private List<String> challengeKeywords;
        @Schema(description = "오늘의 추천 챌린지(리스트)")
        private List<RecommendedChallengeDTO> recommendedChallenges;
        @Schema(description = "챌린지 리포트")
        private ChallengeReportDTO challengeReport;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 선택 후 저장 리스트 response")
    public static class SelectChallengeResponseDTO {
        @Schema(description = "저장한 챌린지 리스트")
        private List<SelectedChallengesInfo> selectedChallenges;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 선택 후 저장 response")
    public static class SelectedChallengesInfo {
        @Schema(description = "저장한 챌린지 id", example = "1")
        private Long id;
        @Schema(description = "저장한 챌린지 타입", example = "DAILY")
        private UserChallengeType challengeType;
        @Schema(description = "저장한 챌린지 제목", example = "좋아하는 책 독서하기")
        private String title;
        @Schema(description = "저장한 챌린지 내용", example = "좋아하는 책을 골라서 한 번 읽어 보세요!")
        private String content;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린치 추천 response")
    public static class RecommendedChallengeDTO {
        @Schema(description = "추천 챌린지 id", example = "1")
        private Long id;
        @Schema(description = "추천 챌린지 제목", example = "좋아하는 책 독서하기")
        private String title;
        @Schema(description = "추천 챌린지 내용", example = "좋아하는 책을 골라서 한 번 읽어 보세요!")
        private String content;
        @Schema(description = "챌린지 타입(DAILY or RANDOM)", example = "DAILY")
        private UserChallengeType challengeType;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
        @Schema(description = "챌린지 완료(인증) 여부", example = "false")
        private boolean completed;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 리포트 response")
    public static class ChallengeReportDTO {
        @Schema(description = "총 크레딧 수", example = "1200")
        private int totalCredits;
        @Schema(description = "작성한 일기 수", example = "16")
        private int totalDiaries;
        @Schema(description = "일기를 작성해온 기간", example = "D+15")
        private String diaryDate;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 리스트 조회 response")
    public static class ChallengeStatusDTO {
        @Schema(description = "챌린지 id", example = "1")
        private Long id;
        @Schema(description = "챌린지 제목", example = "좋아하는 책 독서하기")
        private String title;
        @Schema(description = "챌린지 타입(DAILY or RANDOM)", example = "DAILY")
        private UserChallengeType challengeType;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
        @Schema(description = "챌린지 완료(인증) 여부", example = "false")
        private boolean completed;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 현황 조회 response")
    public static class ChallengeStatusPagedResponseDTO  {
        @Schema(description = "챌린지 목록")
        private List<ChallengeStatusDTO> content;
        @Schema(description = "현재 페이지", example = "1")
        private int currentPage;
        @Schema(description = "전체 페이지", example = "1")
        private int totalPages;
        @Schema(description = "전체 챌린지 수", example = "1")
        private long totalElements;
        @Schema(description = "첫번째 페이지인지", example = "true")
        private boolean isFirst;
        @Schema(description = "마지막 페이지인지", example = "true")
        private boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "presigned url 생성 response")
    public static class ProofPresignedUrlResponseDTO {
        @Schema(description = "S3 업로드용 Presigned URL", example = "https://growit-server-bucket.s3.ap-northeast-2.amazonaws.com/challenges/1842f2aa-40d0-4ae3~")
        private String presignedUrl;
        @Schema(description = "접근 파일명", example = "3c99605a8e01.png")
        private String fileName;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 인증 작성 결과 response")
    public static class CreateProofDTO {
        @Schema(description = "챌린지 인증 내역 id", example = "1")
        private Long id;
        @Schema(description = "챌린지 제목", example = "학교 도서관에서 독서하기")
        private String title;
        @Schema(description = "챌린지 인증 파일명", example = "3c99605a8e01.png")
        private String certificationImageName;
        @Schema(description = "챌린지 인증 소감", example = "오늘은 우주공강에 학교 도서관에 가서~")
        private String thoughts;
        @Schema(description = "챌린지 인증 날짜", example ="2025-08-26T01:11:50")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime certificationDate;
        @Schema(description = "크레딧 정보")
        CreditInfo creditInfo;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreditInfo {
        @Schema(description = "크레딧 지급 여부", example = "true")
        Boolean granted;
        @Schema(description = "지급된 크레딧 양 (지급되지 않은 경우 0)", example = "10")
        Integer amount;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 인증 내역 response")
    public static class ProofDetailsDTO {
        @Schema(description = "챌린지 인증 내역 id", example = "1")
        private Long id;
        @Schema(description = "챌린지 제목", example = "학교 도서관에서 독서하기")
        private String title;
        @Schema(description = "챌린지 인증 이미지 url", example = "https://growit-server-bucket.s3.ap-northeast-2.amazonaws.com/challenges/1842f2aa-40d0-4ae3~")
        private String certificationImageUrl;
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
    @Schema(title = "챌린지 수정 response")
    public static class ModifyProofDTO {
        @Schema(description = "챌린지 인증 파일명", example = "3c99605a8e01.png")
        private String certificationImageName;
        @Schema(description = "챌린지 인증 소감", example = "오늘은 우주공강에 학교 도서관에 가서~")
        private String thoughts;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 삭제 response")
    public static class DeleteChallengeResponseDTO {
        @Schema(description = "사용자 챌린지 id", example = "1")
        private Long id;
        @Schema(description = "챌린지 삭제 성공 메시지", example = "챌린지를 삭제했어요.")
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 추천 response")
    public static class ChallengeDTO {
        @Schema(description = "추천 챌린지 id", example = "1")
        private Long id;
        @Schema(description = "추천 챌린지 제목", example = "좋아하는 책 독서하기")
        private String title;
        @Schema(description = "추천 챌린지 내용", example = "좋아하는 책을 골라서 한 번 읽어 보세요!")
        private String content;
        @Schema(description = "챌린지 소요 시간", example = "1")
        private Integer time;
        @Schema(description = "챌린지 추천 타입(DAILY or RANDOM)", example = "DAILY")
        private UserChallengeType challengeType;
    }
}
