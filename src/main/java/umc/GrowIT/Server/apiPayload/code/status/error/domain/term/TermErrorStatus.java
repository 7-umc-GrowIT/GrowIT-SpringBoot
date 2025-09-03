package umc.GrowIT.Server.apiPayload.code.status.error.domain.term;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum TermErrorStatus implements BaseErrorCode {
    MANDATORY_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM_400_01", "필수 약관에 반드시 동의해야 합니다."),
    ALL_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM_400_02", "전체 약관 정보를 주세요."),

    TERM_NOT_FOUND(HttpStatus.NOT_FOUND, "TERM_404_01", "존재하지 않는 약관을 요청했습니다."),
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
