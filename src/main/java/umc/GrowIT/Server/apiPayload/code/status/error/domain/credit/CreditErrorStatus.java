package umc.GrowIT.Server.apiPayload.code.status.error.domain.credit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum CreditErrorStatus implements BaseErrorCode {
    INSUFFICIENT_CREDIT(HttpStatus.BAD_REQUEST, "CREDIT_400_01", "보유 크레딧이 부족합니다."),
    CREDIT_NOT_FOUND(HttpStatus.NOT_FOUND, "CREDIT_404_01", "크레딧 정보를 찾을 수 없습니다."),
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
