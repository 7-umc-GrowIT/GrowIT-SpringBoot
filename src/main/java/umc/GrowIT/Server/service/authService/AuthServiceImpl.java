package umc.GrowIT.Server.service.authService;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;
import umc.GrowIT.Server.apiPayload.exception.AuthHandler;
import umc.GrowIT.Server.apiPayload.exception.UserHandler;
import umc.GrowIT.Server.converter.AuthenticationCodeConverter;
import umc.GrowIT.Server.domain.AuthenticationCode;
import umc.GrowIT.Server.domain.User;
import umc.GrowIT.Server.domain.enums.AuthType;
import umc.GrowIT.Server.domain.enums.CodeStatus;
import umc.GrowIT.Server.repository.AuthenticationCodeRepository;
import umc.GrowIT.Server.repository.UserRepository;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthRequestDTO;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Value;

import static umc.GrowIT.Server.domain.enums.UserStatus.INACTIVE;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final AuthenticationCodeRepository authenticationCodeRepository;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public AuthResponseDTO.SendAuthEmailResponseDTO sendAuthEmail(AuthType type, AuthRequestDTO.SendAuthEmailRequestDTO request) {
        // 1. type 구분해서 email 체크
        checkEmail(type, request.getEmail());

        // 2. 랜덤 번호 생성
        String authenticationCode = createAuthenticationCode();

        // 3. 이메일 전송
        sendEmail(request.getEmail(), authenticationCode);

        // 4. 인증코드 유효성 체크 & 저장
        AuthenticationCode authCode = saveAuthenticationCode(request.getEmail(), authenticationCode);
        return AuthenticationCodeConverter.toSendAuthCodeResponse(authCode);
    }

    @Override
    public AuthResponseDTO.VerifyAuthCodeResponseDTO verifyAuthCode(AuthRequestDTO.VerifyAuthCodeRequestDTO request) {
        // 1. 이메일 & 유효기간 & status로 entity 조회
        AuthenticationCode authenticationCode = authenticationCodeRepository.findByEmailAndExpirationDateAfterAndStatus(request.getEmail(), LocalDateTime.now(), CodeStatus.ACTIVE)
                .orElseThrow(() -> new AuthHandler(ErrorStatus.AUTH_CODE_NOT_FOUND));

        // 2. request와 인증번호 비교
        if(!authenticationCode.getCode().equals(request.getAuthCode())) {
            throw new AuthHandler(ErrorStatus.AUTH_CODE_MISMATCH);
        }
        else {
            authenticationCode.update();
            authenticationCodeRepository.save(authenticationCode);
            return AuthenticationCodeConverter.toVerifiedAuthCodeResponse(authenticationCode);
        }
    }

    // 이메일 체크
    public void checkEmail(AuthType type, String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null); // 사용자가 존재하지 않으면 null을 반환

        if (type == AuthType.SIGNUP) { //회원가입 (이메일이 존재하면 X)
            if (user != null) {
                throw new UserHandler(ErrorStatus.EMAIL_ALREADY_EXISTS);
            }
        } else if (type == AuthType.PASSWORD_RESET) { //비밀번호 변경 (이메일이 존재해야 함, 탈퇴하지 않은 회원이어야 함)
            if (user == null) {
                throw new UserHandler(ErrorStatus.USER_NOT_FOUND);
            }
            if (user.getStatus() == INACTIVE)
                throw new UserHandler(ErrorStatus.USER_STATUS_INACTIVE);
        } else { //type이 잘못됨
            throw new AuthHandler(ErrorStatus.INVALID_AUTH_TYPE);
        }
    }

    // 랜덤번호 생성
    private String createAuthenticationCode() {
        // 8자리, 문자, 숫자 포함 문자열 생성
        return RandomStringUtils.random(8, true, true);
    }

    // 이메일 전송
    private void sendEmail(String email, String authenticationCode) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            // 이메일 제목 설정
            message.setSubject("GrowIT 인증번호입니다.");

            // 이메일 수신자 설정
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, "", "UTF-8"));

            // 이메일 발신자 설정
            message.setFrom(new InternetAddress(fromEmail, "GrowIT", "UTF-8"));

            // 이메일 내용 설정
            message.setText(setContext(authenticationCode), "UTF-8", "html");

            // 전송
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_SEND_FAIL);
        } catch (UnsupportedEncodingException e) {
            throw new AuthHandler(ErrorStatus.EMAIL_ENCODING_FAIL);
        }
    }

    // 생성해놓은 html에 인증 코드를 넣어서 반환
    private String setContext(String authenticationCode) {
        Context context = new Context();
        context.setVariable("authenticationCode", authenticationCode);
        return templateEngine.process("email-authentication", context);
    }

    // 유효성 체크 & 저장
    private AuthenticationCode saveAuthenticationCode(String email, String authenticationCode) {
        // 1. 이전 유효한 인증 코드가 있는지 확인 & 있다면 무효화
        authenticationCodeRepository
                .findByEmailAndExpirationDateAfterAndStatus(email, LocalDateTime.now(), CodeStatus.ACTIVE)
                        .ifPresent(authCode -> {
                            authCode.updateStatus(CodeStatus.EXPIRED);
                            authenticationCodeRepository.save(authCode);
                        });

        // 2. 새 인증 코드 저장
        AuthenticationCode newAuthenticationCode = AuthenticationCodeConverter.toAuthenticationCode(email, authenticationCode);
        return authenticationCodeRepository.save(newAuthenticationCode);
    }
}
