package umc.GrowIT.Server.web.dto.GroDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GroResponseDTO {

    @Schema(description = "그로 id")
    private Long id;

    @Schema(description = "사용자 id")
    private Long user_id;

    @Schema(description = "사용자가 입력한 그로의 이름")
    private String name;

    @Schema(description = "초기 레벨")
    private Integer level;

    @Schema(description = "생성 시간")
    private String created_at;

    @Schema(description = "수정 시간")
    private String updated_at;
}
