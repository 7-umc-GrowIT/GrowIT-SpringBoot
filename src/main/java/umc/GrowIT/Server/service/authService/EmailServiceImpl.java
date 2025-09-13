package umc.GrowIT.Server.service.authService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;


    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    public void sendEmailAsync(String email, String authenticationCode) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 메일 기본 정보
            helper.setTo(email);
            helper.setFrom(fromEmail, "GrowIT");
            helper.setSubject("[GrowIT] 이메일 인증번호 안내");
            helper.setText(setContext(authenticationCode), true);

            // 메일 발송
            javaMailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {

            log.error("[메일 발송 실패] - email: {}, reason: {}", email, e.getMessage());
        }
    }

    private String setContext(String authenticationCode) {
        Context context = new Context();
        context.setVariable("authenticationCode", authenticationCode);
        return templateEngine.process("email-authentication", context);
    }
}
