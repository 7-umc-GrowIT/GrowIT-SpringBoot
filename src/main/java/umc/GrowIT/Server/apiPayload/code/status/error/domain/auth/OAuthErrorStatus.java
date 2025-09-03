package umc.GrowIT.Server.apiPayload.code.status.error.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum OAuthErrorStatus implements BaseErrorCode {
    ACCOUNT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "OAUTH_400_01", "요청한 이메일로 가입할 수 없습니다."),
    INVALID_AUTHORIZATION_CODE(HttpStatus.BAD_REQUEST, "AUTH_400_02", "유효하지 않은 인가 코드입니다."),
    INVALID_APPLE_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH_400_03", "유효하지 않은 Apple ID Token 입니다."),
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH_400_04", "유효하지 않은 소셜 로그인 제공자입니다."),

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "OAUTH_404_01", "존재하지 않는 계정입니다."),

    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "OAUTH_409_01", "이미 가입한 소셜 계정입니다."),

    INVALID_APPLE_CLIENT_SETTING(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_03", "Apple Client 설정값이 유효하지 않습니다."),
    APPLE_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH_500_04", "ID Token 또는 JWKs 파싱에 실패했습니다."),
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
