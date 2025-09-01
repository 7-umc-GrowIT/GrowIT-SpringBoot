package umc.GrowIT.Server.service.discordService;

public interface DiscordNotificationService {
    void sendErrorNotification(String errorMessage, String requestUri, String method, String userAgent, Exception exception);
}