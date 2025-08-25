package umc.GrowIT.Server.web.dto.GroDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
public class GroRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequestDTO {
        @Schema(description = "성장할 그로의 이름(2~8자)", example = "그로우")
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        @Size(min = 2, max = 8, message = "2글자에서 8글자 사이로 입력해야 합니다.")
        private String name;

        @Schema(description = "배경아이템의 이름", example = "별 배경화면 / 하트 배경화면 / 나무 배경화면")
        private String backgroundItem;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NicknameRequestDTO {
        @Schema(description = "변경하려는 그로의 닉네임(2~8자)", example = "그로우")
        @NotBlank(message = "닉네임은 필수 입력 항목입니다.")
        @Size(min = 2, max = 8, message = "2글자에서 8글자 사이로 입력해야 합니다.")
        private String name;
    }
}