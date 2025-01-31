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
    public static class ItemListDTO {
        @Schema(description = "아이템 목록")
        private List<ItemDTO> itemList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDTO {
        @Schema(description = "아이템 ID")
        Long id;

        @Schema(description = "아이템명")
        String name;

        @Schema(description = "아이템 가격 (크레딧)", example = "120")
        Integer price;

        @Schema(description = "이미지 URL")
        String imageUrl;

        @Schema(description = "상점에서 사용될 경우 배경색")
        String shopBackgroundColor;

        @Schema(description = "아이템 카테고리", allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"})
        String category;

        @Schema(description = "착용 상태", example = "EQUIPPED", allowableValues = {"EQUIPPED","UNEQUIPPED"})
        String status;

        @Schema(description = "구매 여부", example = "false")
        boolean purchased;


    }

    // 아이템 주문 응답 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PurchaseItemResponseDTO {
        private Long itemId;    // 구매한 아이템 ID
        private String itemName; // 구매한 아이템 이름
    }
}
