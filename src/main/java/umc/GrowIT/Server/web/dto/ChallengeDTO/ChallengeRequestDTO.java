package umc.GrowIT.Server.web.dto.ChallengeDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class ChallengeRequestDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofRequestDTO {
        @Schema(description = "인증 이미지 파일 (multipart/form-data)", type = "string", format = "binary")
        @Nullable // 클라이언트가 이미지를 업로드하지 않으면 null 허용
        private MultipartFile certificationImage;
        private String thoughts;
    }

}
