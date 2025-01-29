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
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH4002", "만료된 토큰입니다."),
    UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "AUTH4003", "지원하지 않는 토큰입니다."),
    EMPTY_CLAIMS(HttpStatus.BAD_REQUEST, "AUTH4004", "JWT 토큰의 클레임 문자열이 비어 있습니다."),
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AUTH4005", "이메일 인증을 완료해주세요."),
    INVALID_AUTH_TYPE(HttpStatus.BAD_REQUEST, "AUTH4006", "이메일 인증 타입이 잘못되었습니다."),
    AUTH_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH4007", "유효한 인증번호가 없습니다."),
    AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH4008", "인증번호가 올바르지 않습니다."),
    EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5001", "이메일 전송에 실패했습니다."),
    EMAIL_ENCODING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH5002", "이메일 내용 인코딩에 실패했습니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH4007", "데이터베이스에서 refreshToken을 찾을 수 없습니다."),

    // 챌린지 관련 에러
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE4001", "챌린지를 찾을 수 없습니다."),
    CHALLENGE_VERIFY_NOT_EXISTS(HttpStatus.BAD_REQUEST, "CHALLENGE4002", "챌린지 인증 내역이 존재하지 않습니다."),
    CHALLENGE_USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "CHALLENGE4003", "유저 아이디가 챌린지 아이디와 일치하지 않습니다."),
    CHALLENGE_VERIFY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "CHALLENGE4004", "이미 완료된 챌린지입니다."),

    //기타 에러
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "PWD4001", "비밀번호 확인이 일치하지 않습니다."),

    // 아이템 관련 에러
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ITEM4001", "아이템을 찾을 수 없습니다."),
    ITEM_NOT_OWNED(HttpStatus.BAD_REQUEST, "ITEM4002", "보유하지 않은 아이템입니다."),
    ITEM_OWNED(HttpStatus.BAD_REQUEST, "ITEM4003", "이미 보유 중인 아이템입니다."),

    // 사용자 아이템 관련 에러
    USER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "UI4001", "사용자 아이템이 존재하지 않습니다."),
    EQUIPPED_USER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "UI4002", "착용 중인 사용자 아이템이 존재하지 않습니다."),

    //결제관련에러
    PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "PAYMENT4001", "결제정보가 정확하지 않습니다."),

    // 크레딧 관련 에러
    CREDIT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CREDIT4001", "크레딧 정보를 찾을 수 없습니다."),
    INSUFFICIENT_CREDIT(HttpStatus.BAD_REQUEST, "CREDIT4002", "보유 크레딧이 부족합니다."),

    // 날짜 관련 에러
    DATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "DATE4001", "유효하지 않은 날짜입니다."),
    DATE_IS_AFTER(HttpStatus.BAD_REQUEST, "DATE4002", "날짜는 오늘 이후로 설정할 수 없습니다."),

    //그로관련
    GRO_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "GRO4001", "이미 사용 중인 닉네임입니다."),
    GRO_NICKNAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "Gro4002", "닉네임은 2글자에서 20글자 사이여야 합니다."),
    GRO_NOT_FOUND(HttpStatus.NOT_FOUND, "GRO4003", "그로에 대한 정보가 존재하지 않습니다."),
    GRO_LEVEL_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "GRO5001", "그로 레벨이 유효하지 않습니다."),

    //일기 관련 에러
    DIARY_NOT_FOUND(HttpStatus.BAD_REQUEST, "DIARY4001", "존재하지 않는 일기입니다."),
    DIARY_CHARACTER_LIMIT(HttpStatus.BAD_REQUEST, "DIARY4002", "100자 이내로 작성된 일기입니다."),
    DIARY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "DIARY4003", "해당날짜에 이미 일기가 존재합니다."),

    // s3 관련 에러
    S3_BAD_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "S3_4001", "파일 확장자가 잘못되었습니다."),
    S3_FILE_EMPTY(HttpStatus.BAD_REQUEST, "S3_4002", "파일이 비어 있습니다."),
    S3_FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "S3_5001", "파일 업로드에 실패했습니다."),
    S3_FILE_OVER_SIZE(HttpStatus.BAD_REQUEST, "S3_4003", "파일 크기가 10MB를 초과했습니다.");



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
