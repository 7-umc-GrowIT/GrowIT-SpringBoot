package umc.GrowIT.Server.web.dto.DiscordDTO;

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
        private String content;          // 일반 텍스트 메시지 내용 (최대 2000자)
        private List<EmbedDTO> embeds;   // 임베드 메시지 리스트 (최대 10개)
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbedDTO {
        private String title;                    // 제목
        private String description;              // 설명/내용
        private String color;                    // 좌측 색상바 (16진수 숫자형 문자열, ex: "15158332")
        private List<FieldDTO> fields;           // 필드 리스트 (최대 25개)
        private FooterDTO footer;                // 하단 푸터 정보
        private String timestamp;                // 우측 하단 타임스탬프
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldDTO {
        private String name;      // 필드 이름/제목 (최대 256자)
        private String value;     // 필드 내용/값 (최대 1024자)
        private boolean inline;   // 필드를 한 줄에 나란히 배치할지 여부 (true: 가로배치, false: 세로배치)
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FooterDTO {
        private String text;      // 푸터 텍스트 내용 (최대 2048자)
    }
}