package umc.GrowIT.Server.web.dto.ItemEquipDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(title = "아이템 착용 Request")
public class ItemEquipRequestDTO {


    @Schema(description = "아이템 착용 상태(EQUIPPED -> 해제, UNEQUIPPED -> 착용) ", example = "EQUIPPED", allowableValues = {"EQUIPPED", "UNEQUIPPED"}, required = true)
    @NotNull(message = "착용 상태는 필수입니다.")
    private String status;

}
