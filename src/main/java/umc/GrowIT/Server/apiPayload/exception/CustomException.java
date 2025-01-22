package umc.GrowIT.Server.apiPayload.exception;

import lombok.Getter;
import umc.GrowIT.Server.apiPayload.code.status.ErrorStatus;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus) {
        super(errorStatus.getMessage());
        this.errorStatus = errorStatus;
    }
}

