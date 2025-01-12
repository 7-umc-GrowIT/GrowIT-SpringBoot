package umc.GrowIT.Server.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    //멤버 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4002", "이메일 또는 패스워드가 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4003", "이미 존재하는 이메일입니다."),
    USER_STATUS_INACTIVE(HttpStatus.UNAUTHORIZED, "USER4004", "탈퇴한 회원입니다."),

    //약관 관련 에러
    TERM_NOT_FOUND(HttpStatus.NOT_FOUND, "TERM4001", "존재하지 않는 약관을 요청했습니다."),
    MANDATORY_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM4002", "필수 약관에 반드시 동의해야 합니다."),
    ALL_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM4003", "전체 약관 정보를 주세요."),

    //인증 관련 에러
    INVALID_TOKEN(HttpStatus.NOT_FOUND, "AUTH4001", "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.NOT_FOUND, "AUTH4002", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.NOT_FOUND, "AUTH4003", "지원하지 않는 JWT 토큰입니다."),
    EMPTY_CLAIMS(HttpStatus.NOT_FOUND, "AUTH4004", "JWT 토큰의 클레임 문자열이 비어 있습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AUTH4005", "이메일 인증을 완료해주세요."),


    //기타 에러
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "PWD4001", "비밀번호 확인이 일치하지 않습니다.");

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
