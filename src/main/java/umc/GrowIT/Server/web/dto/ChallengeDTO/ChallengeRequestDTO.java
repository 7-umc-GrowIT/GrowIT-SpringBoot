package umc.GrowIT.Server.web.dto.ChallengeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import umc.GrowIT.Server.domain.enums.UserChallengeType;

import java.time.LocalDate;
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
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "챌린지 선택 후 저장 request")
    public static class SelectChallengeRequestDTO {
        @Schema(description = "저장할 챌린지 아이디(리스트)", example = "[1, 2]")
        private List<Long> challengeIds;
        @Schema(description = "저장할 챌린지의 타입", example = "DAILY")
        private UserChallengeType challengeType;
        @Schema(description = "챌린지를 저장하는 날짜(오늘 날짜가 기본값)", example = "2025-08-26")
        private LocalDate date;
    }
}
