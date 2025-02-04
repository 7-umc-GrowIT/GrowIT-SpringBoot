package umc.GrowIT.Server.apiPayload.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

    private final ErrorStatus errorStatus;

    public JwtAuthenticationException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}