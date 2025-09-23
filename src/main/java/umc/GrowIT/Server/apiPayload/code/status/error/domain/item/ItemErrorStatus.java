package umc.GrowIT.Server.apiPayload.code.status.error.domain.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ItemErrorStatus implements BaseErrorCode {
    ITEM_NOT_OWNED(HttpStatus.BAD_REQUEST, "ITEM_400_01", "보유하지 않은 아이템입니다."),
    ITEM_NOT_EQUIPPED(HttpStatus.BAD_REQUEST, "ITEM_400_02", "착용중인 아이템이 아닙니다."),

    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "ITEM_404_01", "아이템을 찾을 수 없습니다."),

    ITEM_ALREADY_EQUIPPED(HttpStatus.CONFLICT, "ITEM_409_01", "이미 착용중인 아이템입니다."),
    ITEM_OWNED(HttpStatus.CONFLICT, "ITEM_409_02", "이미 보유중인 아이템입니다."),
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
