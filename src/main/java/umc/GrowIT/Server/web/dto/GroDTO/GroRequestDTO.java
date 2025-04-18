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
    @Size(min = 2, max = 10, message = "2글자에서 20글자 사이로 입력해야합니다.")
    private String name;

    @Schema(description = "배경아이템의 이름", example = "별 배경화면 / 하트 배경화면 / 나무 배경화면")
    private String backgroundItem;
}
