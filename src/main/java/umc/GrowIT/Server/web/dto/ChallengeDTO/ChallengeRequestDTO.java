package umc.GrowIT.Server.web.dto.ChallengeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.util.List;

public class ChallengeRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "presigned url 생성 request")
    public static class ProofRequestPresignedUrlDTO {
        @Schema(description = "업로드할 파일의 MIME 타입", example = "image/png")
        @NotBlank(message = "파일의 MIME 타입은 필수 입력입니다.")
        private String contentType;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 인증 작성/수정 request")
    public static class ProofRequestDTO {

        @Schema(description = "업로드할 파일의 이름", example = "3c99605a8e01.png")
        @NotBlank(message = "이미지는 필수 입력입니다.")
        private String certificationImageName;

        @Schema(description = "소감(텍스트)", example = "오늘은 ~")
        @NotBlank(message = "챌린지 한줄소감은 필수로 입력해야 합니다.")
        @Size(min = 50, max = 100, message = "챌린지 한줄소감은 50자~100자 이내로 입력해야 합니다.")
        private String thoughts;

        @Builder.Default
        @Schema(description = "클라이언트 타임존", example = "Asia/Seoul")
        private String timeZone = "Asia/Seoul";

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 선택 후 저장 request")
    public static class SelectChallengesRequestDTO {
        @Schema(description = "일기 ID", example = "123")
        private Long diaryId;

        @Schema(description = "선택한 챌린지들")
        private List<SelectChallengeItemDTO> challenges;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "선택한 챌린지 아이템")
    public static class SelectChallengeItemDTO {
        @Schema(description = "저장할 챌린지 아이디(리스트)", example = "[60, 63]")
        private List<Long> challengeIds;

        @Schema(description = "저장할 챌린지의 타입", example = "DAILY")
        private UserChallengeType challengeType;
    }
}
