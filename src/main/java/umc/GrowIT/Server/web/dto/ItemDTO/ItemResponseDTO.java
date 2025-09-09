package umc.GrowIT.Server.web.dto.ItemDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ItemResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "아이템 목록 조회 response")
    public static class ItemListDTO {
        @Schema(description = "아이템 목록")
        private List<ItemDTO> itemList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "아이템 조회 response")
    public static class ItemDTO {
        @Schema(description = "아이템 ID", example = "1")
        Long id;

        @Schema(description = "아이템명", example = "곰돌이")
        String name;

        @Schema(description = "아이템 가격 (크레딧)", example = "100")
        Integer price;

        @Schema(description = "상점 리스트용 이미지 URL", example = "https://example.com/bear.png")
        String imageUrl;

        @Schema(description = "그로 착용용 이미지 URL", example = "https://example.com/bear.png")
        String groImageUrl;

        @Schema(description = "아이템 카테고리", allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"})
        String category;

        @Schema(description = "착용 상태", example = "EQUIPPED", allowableValues = {"EQUIPPED","UNEQUIPPED"})
        String status;

        @Schema(description = "구매 여부", example = "false")
        boolean purchased;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "아이템 구매 응답 response")
    public static class PurchaseItemResponseDTO {
        @Schema(description = "구매한 아이템 ID", example = "1")
        private Long itemId;
        @Schema(description = "구매한 아이템 이름", example = "곰돌이")
        private String itemName;
    }
}
