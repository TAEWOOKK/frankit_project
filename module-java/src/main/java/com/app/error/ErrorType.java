package com.app.error;

import lombok.Getter;

@Getter
public enum ErrorType {

    // 테스트 용도
    TEST("001", "badrequest exception test"),

    // 인증 및 인가 관련 에러
    TOKEN_EXPIRED("A-001", "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN("A-002", "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION("A-003", "Authorization Header가 빈값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE("A-004", "인증 타입이 Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND("A-005", "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED("A-006", "해당 refresh token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE("A-007", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
    FORBIDDEN_ADMIN("A-008", "관리자 Role이 아닙니다."),
    FORBIDDEN("A-009", "접근 권한이 없습니다."),
    NOT_EXISTS_AUTH_MEMBER("A-010", "인증된 회원이 아닙니다."),
    NOT_ADMIN_MEMBER("A-011", "관리자 권한을 가진 회원만 API 호출이 가능합니다."),
    NOT_SUPER_ADMIN_MEMBER("A-012", "슈퍼 관리자 권한을 가진 회원만 API 호출이 가능합니다."),
    MEMBER_STATUS_STOP("A-013", "현재 사용 중단된 계정입니다."),
    MEMBER_STATUS_DEACTIVATE("A-014", "현재 탈퇴 된 계정입니다."),
    NOT_EXIST_AUTH_REQUEST("A-015", "인증 신청하지 않은 계정입니다."),
    NOT_COMPLETE_AUTH("A-016", "인증을 완료하지 않은 계정입니다."),
    SMS_AUTH_COUNT_VALIDATION("A-017", "SMS 인증은 하루에 최대 5번까지 가능합니다."),
    NOT_VERIFIED_PARTNER("A-018", "관리자가 해당 파트너사의 인증을 완료하지 않았습니다."),
    NOT_EXIST_MEMBER_ADDINFO("A-019", "회원 추가 정보가 존재하지 않습니다."),

    // 회원 관련 에러
    INVALID_MEMBER_TYPE("M-001", "잘못된 회원 타입 입니다.(memberType : KAKAO)"),
    ALREADY_REGISTERED_MEMBER("M-002", "이미 가입된 이메일 입니다."),
    MEMBER_NOT_EXISTS("M-003", "해당 회원은 존재하지 않습니다."),
    NOT_SAME_PASSWORD("M-004", "비밀번호가 같지 않습니다. 비밀번호를 확인해주세요"),
    NOT_VALID_PASSWORD("M-004", "비밀번호가 일치하지 않습니다."),
    NOT_VALID_AUTH_CODE("M-005", "유효하지 않은 인증 코드입니다."),
    EXPIRED_AUTH_CODE("M-006", "인증 시간이 만료됐습니다."),
    NOT_VERIFIED("M-007", "회원 가입 후 인증이 완료되지 않았습니다. 인증을 완료해주세요."),
    ALREADY_DEACTIVATE("M-008", "이미 탈퇴된 회원입니다."),
    NOT_FOURTEEN("M-009", "만 14세 이상만 가입이 가능합니다."),
    NOT_AGREE_PERSONAL_INFO("M-010", "개인정보 수집 및 이용에 동의해주세요."),
    NOT_AGREE_SERVICE_TERMS("M-011", "서비스 이용약관에 동의해주세요."),

    // 이메일 관련 오류
    CREATE_EMAIL_ERROR("EM-001", "메일 전송 중 오류가 발생하였습니다."),

    // SMS 관련 오류
    SMS_SEND_FAIL("SM-001", "SMS 전송 중 오류가 발생하였습니다."),
    SMS_HEADER_CREATE_FAIL("SM-002", "SMS 전송 중 오류가 발생하였습니다."),
    SMS_SEND_RESPONSE_FAIL("SM-003", "SMS 전송 중 오류가 발생하였습니다."),
    SMS_CONTENT_LENGTH_OVER("SM-004", "SMS 길이는 최대 90byte를 넘을 수 없습니다."),
    LMS_CONTENT_LENGTH_OVER("SM-005", "SMS 길이는 최대 2000byte를 넘을 수 없습니다."),
    SMS_SEND_NUMBER_VALIDATION("SM-006", "같은 고객에게 SMS 전송은 하루에 최대 5번까지 가능합니다."),
    SMS_IMAGE_UPLOAD_FAIL("SM-007", "SMS전송 처리 중 오류가 발생하였습니다."),
    SMS_IMAGE_UPLOAD_FILE_NULL("SM-008", "MMS전송 시 이미지는 필수 입력 값입니다."),

    // 기타 에러
    SERVER_ERROR("E-001", "알 수 없는 에러가 발생하였습니다. 잠시 후에 시도해주세요."),
    ILLEGAL_ERROR("E-002", "잘못된 인수가 발견되어 에러가 발생했습니다. 적절한 인수로 시도해주세요."),
    EXTENTION_NOT_EXISTS("E-003", "파일의 확장자가 존재하지 않습니다."),
    EXTENTION_NOT_VALID("E-004", "jpg, jpeg 확장자의 파일만 업로드가 가능합니다."),

    // Amberstudent API 에러
    ILLEGAL_ROOM_TYPE_ERROR("AM-001", "잘못된 룸타입이 발견되어 에러가 발생했습니다. API 명세를 확인해주세요"),

    // 숙소 관련 에러
    NOT_FOUND_HOUSING("H-001", "해당 숙소는 존재하지 않습니다."),
    NOT_FOUND_ROOM_TYPE("H-002", "해당 방 종류는 존재하지 않습니다."),
    NOT_FOUND_COUNTRY("H-003", "해당 국가는 존재하지 않습니다."),
    NOT_FOUND_LOCALITY("H-004", "해당 도시는 존재하지 않습니다."),
    DUPLICATE_ASSIGN_HOUSING("H-005", "이미 등록된 양도 물건입니다."),

    // AWS 에러
    FAIL_UPLOAD("AWS-001", "S3 파일 업로드 실패하였습니다."),

    // 파트너사 관련 에러
    CONFLICT_RECOMMEND_CODE("P-001", "추천 코드가 중복되었습니다."),
    NOT_FOUND_PARTNER("P-002", "해당 파트너사는 존재하지 않습니다."),

    // 상담/계약 관련 에러
	NOT_FOUND_ENGAGE("EN-001", "해당 상담/계약은 존재하지 않습니다."),
    FORBIDDEN_ENGAGE("EN-002", "해당 상담/계약에 접근할 권한이 없습니다."),
    NOT_CHANGE_ENGAGE_STATUS("EN-003", "계약완료된 상태는 변경할 수 없습니다."),

    // 후기 관련 에러
    ILLEGAL_REVIEW_SCORE("R-001", "후기 점수는 0점부터 5점까지만 가능합니다."),
    NOT_FOUND_REVIEW("R-002", "해당 후기는 존재하지 않습니다."),

    // 기타 관리 관련 에러
    NOT_FOUND_BOARD("N-001", "해당 게시글은 존재하지 않습니다."),
    NOt_FOUND_ENTITY("N-002", "해당 엔티티는 존재하지 않습니다.")

    ;

    // 에러 코드 (예: "A-001", "M-002" 등)
    private String errorCode;

    // 에러 메시지 (예: "토큰이 만료되었습니다.", "이미 가입된 회원입니다." 등)
    private String errorMessage;

    /**
     * ErrorType 생성자입니다.
     *
     * @param errorCode 에러 코드
     * @param errorMessage 에러 메시지
     */
    ErrorType(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}

