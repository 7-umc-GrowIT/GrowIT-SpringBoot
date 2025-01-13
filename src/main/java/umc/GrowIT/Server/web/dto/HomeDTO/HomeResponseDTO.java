package umc.GrowIT.Server.web.dto.HomeDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class HomeResponseDTO {

    // 홈 화면 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetHomeResponseDTO {
        private Integer credit; // 보유 크레딧
        private Long itemId;    // 착용 중인 아이템 ID

        // TODO 이미지 처리 방식 확정되면 이후 수정 필요
        private String itemImage; // 착용 중인 아이템 이미지
        private String groImage; // 그로 이미지
    }
}
