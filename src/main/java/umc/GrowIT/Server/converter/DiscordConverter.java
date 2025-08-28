package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.web.dto.DiscordDTO.DiscordRequestDTO;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class DiscordConverter {

    public static DiscordRequestDTO.WebhookRequestDTO toErrorNotificationWebhook(
            String errorMessage, String requestUri, String method, String userAgent, Exception exception) {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        List<DiscordRequestDTO.FieldDTO> fields = Arrays.asList(
                DiscordRequestDTO.FieldDTO.builder()
                        .name("🚨 에러 메시지")
                        .value("```" + (errorMessage.length() > 200 ? errorMessage.substring(0, 200) + "..." : errorMessage) + "```")
                        .inline(false)
                        .build(),
                DiscordRequestDTO.FieldDTO.builder()
                        .name("📍 요청 URI")
                        .value("`" + method + " " + requestUri + "`")
                        .inline(true)
                        .build(),
                DiscordRequestDTO.FieldDTO.builder()
                        .name("🕐 발생 시간")
                        .value(timestamp)
                        .inline(true)
                        .build(),
                DiscordRequestDTO.FieldDTO.builder()
                        .name("🔍 Exception Type")
                        .value("`" + exception.getClass().getSimpleName() + "`")
                        .inline(true)
                        .build()
        );

        DiscordRequestDTO.EmbedDTO embed = DiscordRequestDTO.EmbedDTO.builder()
                .title("🚨 GrowIT 서버 에러 발생")
                .description("**500번대 에러가 발생했습니다. \n 서버 관리자들은 즉각 확인바랍니다.**")
                .color("15158332") // 빨간색
                .fields(fields)
                .footer(DiscordRequestDTO.FooterDTO.builder()
                        .text("GrowIT Server Error Alert")
                        .build())
                .timestamp(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
                .build();

        return DiscordRequestDTO.WebhookRequestDTO.builder()
                .content("@here 서버에서 에러가 발생했습니다! ❌❌")
                .embeds(Arrays.asList(embed))
                .build();
    }
}