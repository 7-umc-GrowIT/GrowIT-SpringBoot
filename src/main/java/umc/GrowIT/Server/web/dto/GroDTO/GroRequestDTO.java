package umc.GrowIT.Server.web.dto.GroDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class GroRequestDTO {

    @Schema(description = "성장할 그로의 이름(최대 50자)", example = "그로이름")
    @NotBlank(message = "name은 필수 입력 항목입니다.")
    @Size(max = 50, message = "name은 최대 50자까지 입력 가능합니다.")
    private String name;
}
