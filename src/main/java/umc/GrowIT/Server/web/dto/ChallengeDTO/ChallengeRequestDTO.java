package umc.GrowIT.Server.web.dto.ChallengeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.util.List;

public class ChallengeRequestDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofRequestDTO {
        @Schema(description = "S3 업로드 후 반환된 URL (Presigned URL 사용 시)")
        private String certificationImageUrl; // Presigned URL 사용 시 S3 URL을 받음
        @Schema(description = "소감 (텍스트)", requiredMode = Schema.RequiredMode.REQUIRED)
        private String thoughts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProofRequestDTO {
        @Schema(description = "S3 업로드 후 반환된 URL (Presigned URL 사용 시)")
        private String certificationImageUrl;
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
    }
}
