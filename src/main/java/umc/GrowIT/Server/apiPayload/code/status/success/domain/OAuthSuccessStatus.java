package umc.GrowIT.Server.apiPayload.code.status.success.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseCode;
import umc.GrowIT.Server.apiPayload.code.ReasonDTO;

@Getter
@AllArgsConstructor
public enum OAuthSuccessStatus implements BaseCode {
    NEED_TO_ACCEPT_TERMS(HttpStatus.ACCEPTED, "OAUTH_202_01", "회원가입을 위해 약관 동의 목록이 추가적으로 필요합니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
