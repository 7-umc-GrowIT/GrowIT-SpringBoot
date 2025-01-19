package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.AuthenticationCode;
import umc.GrowIT.Server.domain.enums.CodeStatus;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;

import java.time.LocalDateTime;

public class AuthenticationCodeConverter {
    public static AuthenticationCode toAuthenticationCode(String email, String authCode) {
        return AuthenticationCode.builder()
                .email(email)
                .code(authCode)
                .isVerified(false)
                .codeStatus(CodeStatus.ACTIVE)
                .expirationDate(LocalDateTime.now().plusMinutes(5)) // 유효기간 5분
                .build()
                ;
    }

    public static AuthResponseDTO.SendAuthEmailResponseDTO toAuthCodeResponse(AuthenticationCode authCode) {
        return AuthResponseDTO.SendAuthEmailResponseDTO.builder()
                .email(authCode.getEmail())
                .message("이메일로 인증번호가 전송되었습니다.")
                .code(authCode.getCode())
                .expiration(authCode.getExpirationDate())
                .build()
                ;
    }
}
