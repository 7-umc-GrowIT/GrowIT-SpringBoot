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


    // 아이템 관련 에러
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ITEM4001", "아이템을 찾을 수 없습니다."),
    ITEM_NOT_OWNED(HttpStatus.BAD_REQUEST, "ITEM4002", "보유하지 않은 아이템입니다."),
    ITEM_ALREADY_EQUIPPED(HttpStatus.BAD_REQUEST, "ITEM4003", "동일 카테고리의 다른 아이템이 이미 착용중입니다."),

    // 크레딧 관련 에러
    CREDIT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CREDIT4001", "크레딧 정보를 찾을 수 없습니다."),
    INSUFFICIENT_CREDIT(HttpStatus.BAD_REQUEST, "CREDIT4002", "보유 크레딧이 부족합니다.");

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
