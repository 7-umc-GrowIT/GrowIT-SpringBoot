package umc.GrowIT.Server.apiPayload.code.status.error.domain.diary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum DiaryErrorStatus implements BaseErrorCode {
    DATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "DIARY_400_01", "유효하지 않은 날짜입니다."),
    DATE_IS_AFTER(HttpStatus.BAD_REQUEST, "DIARY_400_02", "날짜는 오늘 이후로 설정할 수 없습니다."),
    DIARY_CHARACTER_LIMIT(HttpStatus.BAD_REQUEST, "DIARY_400_03", "100자 이내로 작성된 일기입니다."),
    DIARY_SAME_CONTENT(HttpStatus.BAD_REQUEST, "DIARY_400_04", "기존 일기와 동일한 내용입니다."),

    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "DIARY_404_01", "존재하지 않는 일기입니다."),

    DIARY_ALREADY_EXISTS(HttpStatus.CONFLICT, "DIARY_409_01", "해당날짜에 이미 일기가 존재합니다."),
    ANALYZED_DIARY(HttpStatus.CONFLICT, "DIARY_409_02", "이미 분석된 일기입니다."),
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
