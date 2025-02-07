package umc.GrowIT.Server.web.dto.ChallengeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.lang.Nullable;
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
        @Schema(description = "인증 이미지 파일 (multipart/form-data)", type = "string", format = "binary")
        private MultipartFile certificationImage;
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


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecommendChallengesRequestDTO {
        @NotNull
        String diaryContent; //일기 내용
    }
}
