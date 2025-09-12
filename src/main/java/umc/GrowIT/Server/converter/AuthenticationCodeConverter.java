package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.AuthenticationCode;

import java.time.LocalDateTime;

public class AuthenticationCodeConverter {
    public static AuthenticationCode toAuthenticationCode(String email, String authCode) {
        return AuthenticationCode.builder()
                .email(email)
                .code(authCode)
                .isVerified(false)
                .expirationDate(LocalDateTime.now().plusSeconds(62)) // 유효시간 1분 (+ 약간 여유)
                .build()
                ;
    }
}
