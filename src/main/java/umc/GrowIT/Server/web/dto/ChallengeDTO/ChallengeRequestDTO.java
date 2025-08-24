package umc.GrowIT.Server.web.dto.ChallengeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.time.LocalDate;
import java.util.List;

public class ChallengeRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofRequestPresignedUrlDTO {
        @NotBlank(message = "파일의 MIME 타입은 필수 입력입니다")
        @Schema(description = "업로드할 파일의 MIME 타입", example = "image/png")
        private String contentType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofRequestDTO {
        @Schema(description = "S3 업로드 후 반환된 URL (Presigned URL 사용 시)")
        private String certificationImage; // Presigned URL 사용 시 S3 URL을 받음
        @Schema(description = "소감 (텍스트)")
        private String thoughts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SelectChallengeRequestDTO {
        private List<Long> challengeIds;
        private UserChallengeType dtype;
        private LocalDate date;
    }
}
