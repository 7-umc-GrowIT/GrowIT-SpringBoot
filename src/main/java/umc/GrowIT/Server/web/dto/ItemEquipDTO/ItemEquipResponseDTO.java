package umc.GrowIT.Server.web.dto.ItemEquipDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemEquipResponseDTO {
    @Schema(
            description = "아이템 ID",
            example = "1"
    )
    private Long itemId;

    @Schema(
            description = "아이템명",
            example = "블루 배경화면"
    )
    private String name;

    @Schema(
            description = "아이템 카테고리",
            example = "BACKGROUND",
            allowableValues = {"BACKGROUND", "OBJECT", "PLANT", "HEAD_ACCESSORY"}
    )
    private String category;

    @Schema(
            description = "착용 상태",
            example = "EQUIPPED",
            allowableValues = {"EQUIPPED", "UNEQUIPPED"}
    )
    private String status;

    @Schema(
            description = "상태 변경 시간",
            example = "2024-01-11T15:30:00"
    )
    private String updatedAt;
}
