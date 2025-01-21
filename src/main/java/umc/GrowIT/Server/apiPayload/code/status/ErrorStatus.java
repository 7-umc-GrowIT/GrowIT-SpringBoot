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
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER4001", "비밀번호 확인이 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "USER4002", "이메일 또는 패스워드가 일치하지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER4003", "이미 존재하는 이메일입니다."),
    USER_STATUS_INACTIVE(HttpStatus.UNAUTHORIZED, "USER4004", "탈퇴한 회원입니다."),

    //사용자 챌린지 관련 에러
    USER_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "UC4001", "사용자 챌린지가 존재하지 않습니다"),
    USER_CHALLENGE_COMPLETE(HttpStatus.BAD_REQUEST, "UC4002", "완료된 챌린지는 삭제가 불가합니다"),

    //약관 관련 에러
    TERM_NOT_FOUND(HttpStatus.NOT_FOUND, "TERM4001", "존재하지 않는 약관을 요청했습니다."),
    MANDATORY_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM4002", "필수 약관에 반드시 동의해야 합니다."),
    ALL_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM4003", "전체 약관 정보를 주세요."),

    //인증 관련 에러
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4001", "유효하지 않은 JWT 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4002", "만료된 JWT 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4003", "지원하지 않는 JWT 토큰입니다."),
    EMPTY_CLAIMS(HttpStatus.BAD_REQUEST, "AUTH4004", "JWT 토큰의 클레임 문자열이 비어 있습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AUTH4005", "이메일 인증을 완료해주세요."),
    INVALID_AUTH_TYPE(HttpStatus.BAD_REQUEST, "AUTH4006", "이메일 인증 타입이 잘못되었습니다."),
    AUTH_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH4007", "유효한 인증번호가 없습니다."),
    AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH4008", "인증번호가 올바르지 않습니다."),
    EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5001", "이메일 전송에 실패했습니다."),
    EMAIL_ENCODING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5002", "이메일 내용 인코딩에 실패했습니다."),

    // 챌린지 관련 에러
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE4001", "챌린지를 찾을 수 없습니다."),

    //기타 에러
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "PWD4001", "비밀번호 확인이 일치하지 않습니다."),

    // 아이템 관련 에러
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ITEM4001", "아이템을 찾을 수 없습니다."),
    ITEM_NOT_OWNED(HttpStatus.BAD_REQUEST, "ITEM4002", "보유하지 않은 아이템입니다."),
    ITEM_OWNED(HttpStatus.BAD_REQUEST, "ITEM4003", "이미 보유 중인 아이템입니다."),

    //결제관련에러
    PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "PAYMENT4001", "결제정보가 정확하지 않습니다."),

    // 크레딧 관련 에러
    CREDIT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CREDIT4001", "크레딧 정보를 찾을 수 없습니다."),
    INSUFFICIENT_CREDIT(HttpStatus.BAD_REQUEST, "CREDIT4002", "보유 크레딧이 부족합니다."),

    // 날짜 관련 에러
    DATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "DATE4001", "유효하지 않은 날짜입니다."),

    //일기 관련 에러
    DIARY_NOT_FOUND(HttpStatus.BAD_REQUEST, "DIARY4001", "존재하지 않는 일기입니다."),
    DIARY_CHARACTER_LIMIT(HttpStatus.BAD_REQUEST, "DIARY4002", "100자 이내로 작성된 일기입니다.");


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
