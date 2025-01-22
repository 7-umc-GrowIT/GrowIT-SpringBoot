package umc.GrowIT.Server.web.dto.GroDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GroRequestDTO {

    @Schema(description = "성장할 그로의 이름(최대 50자)", example = "그로이름")
    @NotBlank(message = "name은 필수 입력 항목입니다.")
    @Size(max = 50, message = "name은 최대 50자까지 입력 가능합니다.")
    private String name;

    @Schema(description = "배경아이템의 이름", example = "별")
    private String backgroundItem;
}
