package umc.GrowIT.Server.web.dto.DiscordDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class DiscordRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WebhookRequestDTO {
        @Schema(description = "일반 텍스트 메시지 내용 (최대 2000자)")
        private String content;
        @Schema(description = "임베드 메시지 리스트 (최대 10개)")
        private List<EmbedDTO> embeds;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbedDTO {
        @Schema(description = "제목")
        private String title;
        @Schema(description = "설명/내용")
        private String description;
        @Schema(description = "좌측 색상바 (16진수 숫자형 문자열, ex: 15158332)")
        private String color;
        @Schema(description = "필드 리스트 (최대 25개)")
        private List<FieldDTO> fields;
        @Schema(description = "하단 푸터 정보")
        private FooterDTO footer;
        @Schema(description = "우측 하단 타임스탬프")
        private String timestamp;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldDTO {
        @Schema(description = "필드 이름/제목 (최대 256자)")
        private String name;
        @Schema(description = "필드 내용/값 (최대 1024자)")
        private String value;
        @Schema(description = "필드를 한 줄에 나란히 배치할지 여부 (true: 가로배치, false: 세로배치)", example = "true")
        private boolean inline;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FooterDTO {
        @Schema(description = "푸터 텍스트 내용 (최대 2048자)")
        private String text;
    }
}