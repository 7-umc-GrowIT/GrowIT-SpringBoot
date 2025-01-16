package umc.GrowIT.Server.web.dto.ChallengeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ChallengeRequestDTO {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProofRequestDTO {
        private String certificationImage;
        private String thoughts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequestDTO {
        private String certificationImage;
        private String thoughts;
    }
}
