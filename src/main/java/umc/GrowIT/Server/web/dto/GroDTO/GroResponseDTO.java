package umc.GrowIT.Server.web.dto.GroDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.GrowIT.Server.domain.enums.ItemCategory;
import umc.GrowIT.Server.web.dto.ItemDTO.ItemResponseDTO;

import java.util.List;

@Getter
@Builder
public class GroResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "그로 생성 response")
    public static class CreateResponseDTO {
        @Schema(description = "그로 id", example = "1")
        private Long id;

        @Schema(description = "사용자 id", example = "1")
        private Long user_id;

        @Schema(description = "사용자가 입력한 그로의 이름" , example = "그루")
        private String name;

        @Schema(description = "초기 레벨", example = "1")
        private Integer level;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "그로와 착용 아이템 이미지 조회 Response")
    public static class GroAndEquippedItemsDTO {
        @Schema(description = "그로 정보")
        private GroDTO gro;

        @Schema(description = "착용 아이템 정보")
        private List<EquippedItemsDTO> equippedItems;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "그로 정보")
    public static class GroDTO {
        @Schema(description = "그로 레벨", example = "1")
        private Integer level;

        @Schema(description = "그로 이미지 URL", example = "https://example.com/gro.png")
        private String groImageUrl;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(title = "착용 아이템 정보")
    public static class EquippedItemsDTO {
        @Schema(description = "사용자 아이템 ID", example = "1")
        private Long id;

        @Schema(description = "아이템 이름", example = "체리")
        private String name;

        @Schema(description = "아이템 카테고리")
        private ItemCategory category;

        @Schema(description = "아이템 이미지 URL", example = "https://example.com/item.png")
        private String itemImageUrl;
    }
}
