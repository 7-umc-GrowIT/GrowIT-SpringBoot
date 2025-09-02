package umc.GrowIT.Server.apiPayload.code.status;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
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
    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "USER40001", "비밀번호 확인이 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "USER40102", "이메일 또는 패스워드가 일치하지 않습니다."),
    USER_STATUS_INACTIVE(HttpStatus.FORBIDDEN, "USER40304", "탈퇴한 회원입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER40903", "이미 존재하는 이메일입니다."),

    //사용자 챌린지 관련 에러
    USER_CHALLENGE_COMPLETE(HttpStatus.BAD_REQUEST, "UC40001", "완료된 챌린지는 삭제가 불가합니다"),
    USER_CHALLENGE_ALREADY_PROVED(HttpStatus.BAD_REQUEST, "UC40002", "이미 완료된 챌린지입니다."),
    USER_CHALLENGE_NOT_PROVED(HttpStatus.BAD_REQUEST, "UC40003", "챌린지 인증이 완료되지 않았습니다."),
    USER_CHALLENGE_UPDATE_NO_CHANGES(HttpStatus.BAD_REQUEST, "UC40004", "챌린지 인증 내역에 수정사항이 없습니다."),
    USER_CHALLENGE_PROVED_LIMIT(HttpStatus.BAD_REQUEST, "UC40005", "하루에 10번만 인증이 가능합니다."),
    USER_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "UC40401", "사용자 챌린지가 존재하지 않습니다"),

    //약관 관련 에러
    MANDATORY_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM40001", "필수 약관에 반드시 동의해야 합니다."),
    ALL_TERMS_REQUIRED(HttpStatus.BAD_REQUEST, "TERM40002", "전체 약관 정보를 주세요."),
    TERM_NOT_FOUND(HttpStatus.NOT_FOUND, "TERM40401", "존재하지 않는 약관을 요청했습니다."),

    //인증 관련 에러
    EMAIL_NOT_VERIFIED(HttpStatus.BAD_REQUEST, "AUTH40001", "이메일 인증을 완료해주세요."),
    INVALID_AUTH_TYPE(HttpStatus.BAD_REQUEST, "AUTH40002", "이메일 인증 타입이 잘못되었습니다."),
    AUTH_CODE_NOT_FOUND(HttpStatus.BAD_REQUEST, "AUTH40003", "유효한 인증번호가 없습니다."),
    AUTH_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "AUTH40004", "인증번호가 올바르지 않습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH40101", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH40102", "만료된 토큰입니다. 토큰의 만료 시간이 지나 더 이상 유효하지 않습니다."),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH40103", "지원하지 않는 토큰입니다. 서버가 처리할 수 없는 형식의 토큰입니다."),
    MALFORMED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH40104", "손상된 토큰입니다. 토큰 구조가 올바르지 않거나, 일부 데이터가 손실되었습니다."),
    MISSING_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH40105", "토큰이 제공되지 않았습니다."),
    INVALID_TOKEN_FORMAT(HttpStatus.UNAUTHORIZED, "AUTH40106", "잘못된 토큰 형식입니다. 토큰이 Base64 URL 인코딩 규칙을 따르지 않거나, 토큰 내부 JSON 구조가 잘못 되었습니다."),
    INVALID_SIGNATURE(HttpStatus.UNAUTHORIZED, "AUTH40107", "토큰의 서명이 올바르지 않습니다. 서명이 서버에서 사용하는 비밀키와 일치하지 않습니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "AUTH40401", "데이터베이스에서 refreshToken을 찾을 수 없습니다."),

    EMAIL_SEND_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH50001", "이메일 전송에 실패했습니다."),
    EMAIL_ENCODING_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH50002", "이메일 내용 인코딩에 실패했습니다."),

    // 챌린지 관련 에러
    CHALLENGE_SAVE_LIMIT(HttpStatus.BAD_REQUEST, "CHALLENGE40001", "챌린지는 3개까지 저장 가능합니다."),
    CHALLENGE_AT_LEAST(HttpStatus.BAD_REQUEST, "CHALLENGE40002", "최소 하나의 챌린지를 선택해야 합니다."),
    CHALLENGE_DAILY_MAX(HttpStatus.BAD_REQUEST, "CHALLENGE40003", "데일리 챌린지는 최대 2개까지 저장 가능합니다."),
    CHALLENGE_RANDOM_MAX(HttpStatus.BAD_REQUEST, "CHALLENGE40004", "랜덤 챌린지는 최대 1개만 저장 가능합니다."),
    CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE40401", "챌린지를 찾을 수 없습니다."),
    RELATED_CHALLENGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CHALLENGE40402", "연관된 챌린지가 없습니다."),

    //기타 에러
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "PWD40001", "비밀번호 확인이 일치하지 않습니다."),

    // 아이템 관련 에러
    ITEM_NOT_FOUND(HttpStatus.BAD_REQUEST, "ITEM40001", "아이템을 찾을 수 없습니다."),
    ITEM_NOT_OWNED(HttpStatus.BAD_REQUEST, "ITEM40002", "보유하지 않은 아이템입니다."),
    ITEM_OWNED(HttpStatus.BAD_REQUEST, "ITEM40003", "이미 보유 중인 아이템입니다."),
    ITEM_ALREADY_EQUIPPED(HttpStatus.BAD_REQUEST, "ITEM40004", "이미 착용중인 아이템입니다."),
    ITEM_NOT_EQUIPPED(HttpStatus.BAD_REQUEST, "ITEM40005", "착용중인 아이템이 아닙니다."),

    // 사용자 아이템 관련 에러
    EQUIPPED_USER_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "UI40401", "착용 중인 사용자 아이템이 존재하지 않습니다."),

    //결제 관련 에러
    PAYMENT_ERROR(HttpStatus.BAD_REQUEST, "PAYMENT40001", "결제정보가 정확하지 않습니다."),

    // 크레딧 관련 에러
    CREDIT_NOT_FOUND(HttpStatus.BAD_REQUEST, "CREDIT40001", "크레딧 정보를 찾을 수 없습니다."),
    INSUFFICIENT_CREDIT(HttpStatus.BAD_REQUEST, "CREDIT40002", "보유 크레딧이 부족합니다."),

    // 날짜 관련 에러
    DATE_NOT_FOUND(HttpStatus.BAD_REQUEST, "DATE40001", "유효하지 않은 날짜입니다."),
    DATE_IS_AFTER(HttpStatus.BAD_REQUEST, "DATE40002", "날짜는 오늘 이후로 설정할 수 없습니다."),

    //그로 관련
    GRO_NICKNAME_LENGTH_INVALID(HttpStatus.BAD_REQUEST, "GRO40001", "닉네임은 2~8자 이내로 작성해야 합니다."),
    GRO_NICKNAME_UPDATE_NO_CHANGE(HttpStatus.BAD_REQUEST, "GRO40002", "그로의 닉네임에 수정사항이 없습니다."),
    GRO_NOT_FOUND(HttpStatus.NOT_FOUND, "GRO40401", "그로에 대한 정보가 존재하지 않습니다."),
    GRO_ALREADY_EXISTS(HttpStatus.CONFLICT, "GRO40901", "사용자의 그로가 이미 존재합니다."),
    GRO_NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "GRO40902", "다른 닉네임과 중복되는 닉네임입니다."),
    GRO_LEVEL_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "GRO50001", "그로 레벨이 유효하지 않습니다."),

    //일기 관련 에러
    DIARY_NOT_FOUND(HttpStatus.BAD_REQUEST, "DIARY40001", "존재하지 않는 일기입니다."),
    DIARY_CHARACTER_LIMIT(HttpStatus.BAD_REQUEST, "DIARY40002", "100자 이내로 작성된 일기입니다."),
    DIARY_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "DIARY40003", "해당날짜에 이미 일기가 존재합니다."),
    DIARY_SAME_CONTENT(HttpStatus.BAD_REQUEST, "DIARY40004", "기존 일기와 동일한 내용입니다."),
    ANALYZED_DIARY(HttpStatus.BAD_REQUEST, "DIARY40005", "이미 분석된 일기입니다."),

    // S3 관련 에러
    S3_BAD_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "S340001", "파일 확장자가 잘못되었습니다."),
    S3_FILE_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "S340002", "파일 이름은 필수입니다."),
    S3_FOLDER_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "S340003", "폴더 이름은 필수입니다."),
    S3_INVALID_FOLDER_NAME(HttpStatus.BAD_REQUEST, "S340004", "폴더명은 영어로 입력해야 합니다."),

    // OAuth 관련 에러
    ACCOUNT_BAD_REQUEST(HttpStatus.BAD_REQUEST, "OAUTH40003", "요청한 이메일로 가입할 수 없습니다."),
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "OAUTH40402", "존재하지 않는 계정입니다."),
    ACCOUNT_ALREADY_EXISTS(HttpStatus.CONFLICT, "OAUTH40901", "이미 가입한 소셜 계정입니다."),

    INVALID_AUTHORIZATION_CODE(HttpStatus.BAD_REQUEST, "AUTH40005", "유효하지 않은 인가 코드입니다."),
    INVALID_APPLE_ID_TOKEN(HttpStatus.BAD_REQUEST, "AUTH40006", "유효하지 않은 Apple ID Token 입니다."),
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "AUTH40007", "유효하지 않은 소셜 로그인 제공자입니다."),
    INVALID_APPLE_CLIENT_SETTING(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH50003", "Apple Client 설정값이 유효하지 않습니다."),
    APPLE_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AUTH50004", "ID Token 또는 JWKs 파싱에 실패했습니다."),

    // 감정 관련 에러
    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "KEYWORD40401", "감정키워드가 존재하지 않습니다."),

    // GPT 관련 에러
    GPT_RESPONSE_EMPTY(HttpStatus.INTERNAL_SERVER_ERROR, "GPT50001", "GPT 응답이 비어있습니다. 다시 시도해 주세요."),
    EMOTIONS_COUNT_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, "GPT50002", "GPT 응답에서 감정의 개수는 3개여야 합니다."),
    EMOTIONS_DUPLICATE(HttpStatus.INTERNAL_SERVER_ERROR, "GPT50003", "GPT 응답에서 중복된 감정이 존재합니다."),

    // 플라스크 관련 에러
    FLASK_API_CALL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FLASK50001", "Flask API 호출에 실패하였습니다."),

    // 탈퇴 관련 에러
    WITHDRAWAL_REASON_NOT_FOUND(HttpStatus.NOT_FOUND, "WITHDRAWAL40401", "존재하지 않는 탈퇴 사유입니다."),

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
