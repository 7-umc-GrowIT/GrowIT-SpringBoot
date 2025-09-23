package umc.GrowIT.Server.apiPayload.code.status.error.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum UserErrorStatus implements BaseErrorCode {
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER_400_01", "비밀번호 확인이 일치하지 않습니다."), // 확인 필요
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "USER_400_02", "비밀번호 확인이 일치하지 않습니다."),

    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "USER_401_01", "이메일 또는 패스워드가 일치하지 않습니다."),

    USER_STATUS_INACTIVE(HttpStatus.FORBIDDEN, "USER_403_01", "탈퇴한 회원입니다."),

    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_409_01", "이미 존재하는 이메일입니다."),
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
