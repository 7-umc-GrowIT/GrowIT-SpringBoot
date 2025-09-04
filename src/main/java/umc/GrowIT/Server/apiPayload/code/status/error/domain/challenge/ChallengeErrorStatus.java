package umc.GrowIT.Server.apiPayload.code.status.error.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ChallengeErrorStatus implements BaseErrorCode {
    CHALLENGE_SAVE_LIMIT(HttpStatus.BAD_REQUEST, "CHALLENGE_400_01", "챌린지는 3개까지 저장 가능합니다."),
    CHALLENGE_AT_LEAST(HttpStatus.BAD_REQUEST, "CHALLENGE_400_02", "최소 하나의 챌린지를 선택해야 합니다."),
    CHALLENGE_DAILY_MAX(HttpStatus.BAD_REQUEST, "CHALLENGE_400_03", "데일리 챌린지는 최대 2개까지 저장 가능합니다."),
    CHALLENGE_RANDOM_MAX(HttpStatus.BAD_REQUEST, "CHALLENGE_400_04", "랜덤 챌린지는 최대 1개만 저장 가능합니다."),

    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_404_01", "챌린지를 찾을 수 없습니다."),
    RELATED_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE_404_02", "연관된 챌린지가 없습니다."),
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
