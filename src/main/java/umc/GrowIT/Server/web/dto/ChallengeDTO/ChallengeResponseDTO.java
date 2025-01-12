package umc.GrowIT.Server.web.dto.ChallengeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChallengeResponseDTO {

    // 챌린지 삭제 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteChallengeResponseDTO {
        // TODO 디테일하게 결정 필요
        private String message; // ex) 챌린지 삭제가 완료되었습니다
    }
}
