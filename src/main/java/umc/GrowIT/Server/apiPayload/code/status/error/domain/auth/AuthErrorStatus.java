package umc.GrowIT.Server.apiPayload.code.status.error.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum AuthErrorStatus implements BaseErrorCode {
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AUTH_400_01", "이메일 인증을 완료해주세요."),
    INVALID_AUTH_TYPE(HttpStatus.BAD_REQUEST, "AUTH_400_02", "이메일 인증 타입이 잘못되었습니다."),
    AUTH_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH_400_03", "유효한 인증번호가 없습니다."),
    AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH_400_04", "인증번호가 올바르지 않습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_01", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_02", "만료된 토큰입니다. 토큰의 만료 시간이 지나 더 이상 유효하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_03", "지원하지 않는 토큰입니다. 서버가 처리할 수 없는 형식의 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_04", "손상된 토큰입니다. 토큰 구조가 올바르지 않거나, 일부 데이터가 손실되었습니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_401_05", "토큰이 제공되지 않았습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH_401_06", "잘못된 토큰 형식입니다. 토큰이 Base64 URL 인코딩 규칙을 따르지 않거나, 토큰 내부 JSON 구조가 잘못 되었습니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "AUTH_401_07", "토큰의 서명이 올바르지 않습니다. 서명이 서버에서 사용하는 비밀키와 일치하지 않습니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH_404_01", "데이터베이스에서 refreshToken을 찾을 수 없습니다."),

    EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_01", "이메일 전송에 실패했습니다."),
    EMAIL_ENCODING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_02", "이메일 내용 인코딩에 실패했습니다."),
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
