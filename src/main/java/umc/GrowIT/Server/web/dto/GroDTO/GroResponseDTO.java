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
    public static class CreateResponseDTO {
        @Schema(description = "그로 id")
        private Long id;

        @Schema(description = "사용자 id")
        private Long user_id;

        @Schema(description = "사용자가 입력한 그로의 이름")
        private String name;

        @Schema(description = "초기 레벨")
        private Integer level;
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroAndEquippedItemsDTO {
        private GroDTO gro;
        private List<EquippedItemsDTO> equippedItems;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GroDTO {
        private Integer level; // 그로 레벨
        private String groImageUrl; // 그로 이미지 URL
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquippedItemsDTO {
        private String name; // 아이템 이름
        private ItemCategory category; // 아이템 카테고리
        private String itemImageUrl; // 아이템 이미지 URL
    }
}
