package umc.GrowIT.Server.apiPayload.code.status.error.domain.challenge;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import umc.GrowIT.Server.apiPayload.code.BaseErrorCode;
import umc.GrowIT.Server.apiPayload.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum UserChallengeErrorStatus implements BaseErrorCode {
    USER_CHALLENGE_NOT_PROVED(HttpStatus.BAD_REQUEST, "UC_400_01", "챌린지 인증이 완료되지 않았습니다."),
    USER_CHALLENGE_UPDATE_NO_CHANGES(HttpStatus.BAD_REQUEST, "UC_400_02", "챌린지 인증 내역에 수정사항이 없습니다."),
    USER_CHALLENGE_PROVED_LIMIT(HttpStatus.BAD_REQUEST, "UC_400_03", "하루에 10번만 인증이 가능합니다."),

    USER_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "UC_404_01", "사용자 챌린지가 존재하지 않습니다"),

    USER_CHALLENGE_COMPLETE(HttpStatus.CONFLICT, "UC_409_01", "완료된 챌린지는 삭제가 불가합니다"),
    USER_CHALLENGE_ALREADY_PROVED(HttpStatus.CONFLICT, "UC_409_02", "이미 완료된 챌린지입니다."),
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
