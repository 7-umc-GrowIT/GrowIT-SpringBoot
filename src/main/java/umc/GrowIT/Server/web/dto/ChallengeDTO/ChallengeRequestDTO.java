package umc.GrowIT.Server.web.dto.ChallengeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;
>>>>>>> b62f6431a43d92ae7196ca726ae795fc09e7c97c
import org.springframework.web.multipart.MultipartFile;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.time.LocalDate;
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
    public static class SelectChallengeRequestDTO {
        private List<Long> challengeIds;
        private UserChallengeType dtype;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendChallengesRequestDTO {
        @NotNull
        String diaryContent; //일기 내용
    }
}
