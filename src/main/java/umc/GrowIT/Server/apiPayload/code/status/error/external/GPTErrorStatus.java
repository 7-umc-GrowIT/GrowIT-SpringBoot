package umc.GrowIT.Server.apiPayload.code.status.error.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum GPTErrorStatus implements BaseErrorCode {
    GPT_RESPONSE_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "GPT_500_01", "GPT 응답이 비어있습니다. 다시 시도해 주세요."),
    EMOTIONS_COUNT_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "GPT_500_02", "GPT 응답에서 감정의 개수는 3개여야 합니다."),
    EMOTIONS_DUPLICATE(HttpStatus.INTERNAL_SERVER_ERROR, "GPT_500_03", "GPT 응답에서 중복된 감정이 존재합니다."),
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
