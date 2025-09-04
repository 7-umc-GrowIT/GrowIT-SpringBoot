package umc.GrowIT.Server.apiPayload.code.status.error.domain.gro;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum GroErrorStatus implements BaseErrorCode {
    GRO_NICKNAME_UPDATE_NO_CHANGE(HttpStatus.BAD_REQUEST, "GRO_400_01", "그로의 닉네임에 수정사항이 없습니다."),

    GRO_NOT_FOUND(HttpStatus.NOT_FOUND, "GRO_404_01", "그로에 대한 정보가 존재하지 않습니다."),

    GRO_ALREADY_EXISTS(HttpStatus.CONFLICT, "GRO_409_01", "사용자의 그로가 이미 존재합니다."),
    GRO_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "GRO_409_02", "다른 닉네임과 중복되는 닉네임입니다."),

    GRO_LEVEL_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "GRO_500_01", "그로 레벨이 유효하지 않습니다."),
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
