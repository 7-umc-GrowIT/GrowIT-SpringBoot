package umc.GrowIT.Server.converter;

import umc.GrowIT.Server.domain.AuthenticationCode;
import umc.GrowIT.Server.web.dto.AuthDTO.AuthResponseDTO;

import java.time.LocalDateTime;

public class AuthenticationCodeConverter {
    public static AuthenticationCode toAuthenticationCode(String email, String authCode) {
        return AuthenticationCode.builder()
                .email(email)
                .code(authCode)
                .isVerified(false)
                .expirationDate(LocalDateTime.now().plusMinutes(1))
                .build()
                ;
    }

    public static AuthResponseDTO.SendAuthEmailResponseDTO toSendAuthCodeResponse(AuthenticationCode authCode) {
        return AuthResponseDTO.SendAuthEmailResponseDTO.builder()
                .expiration(authCode.getExpirationDate())
                .build()
                ;
    }
}
