package umc.GrowIT.Server.service.discordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import umc.GrowIT.Server.converter.DiscordConverter;
import umc.GrowIT.Server.web.dto.DiscordDTO.DiscordRequestDTO;

@Slf4j
@Service
public class DiscordNotificationServiceImpl implements DiscordNotificationService {

    private final RestTemplate restTemplate;

    @Value("${discord.webhook.url:}")
    private String webhookUrl;

    @Value("${discord.notification.enabled:false}")
    private boolean notificationEnabled;

    public DiscordNotificationServiceImpl(@Qualifier("discordRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Async("discordNotificationExecutor")
    public void sendErrorNotification(String errorMessage, String requestUri, String method, String userAgent, Exception exception) {
        if (!notificationEnabled || webhookUrl.isEmpty()) {
            log.warn("Discord 알림이 비활성화되어 있거나 Webhook URL이 설정되지 않았습니다.");
            return;
        }

        try {
            DiscordRequestDTO.WebhookRequestDTO webhook = DiscordConverter.toErrorNotificationWebhook(
                    errorMessage, requestUri, method, userAgent, exception
            );

            sendToDiscord(webhook);
            log.info("Discord 에러 알림 전송 완료: {}", errorMessage);

        } catch (Exception e) {
            log.error("Discord 알림 전송 실패", e);
        }
    }

    private void sendToDiscord(DiscordRequestDTO.WebhookRequestDTO webhook) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DiscordRequestDTO.WebhookRequestDTO> request = new HttpEntity<>(webhook, headers);

        restTemplate.postForEntity(webhookUrl, request, String.class);
    }
}