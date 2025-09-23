package umc.GrowIT.Server.service.authService;

public interface EmailService {
    void sendEmailAsync(String email, String authenticationCode);
}
