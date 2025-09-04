package umc.GrowIT.Server.apiPayload.code.status.error.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum PaymentErrorStatus implements BaseErrorCode {
    PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "PAYMENT_400_01", "결제정보가 정확하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
