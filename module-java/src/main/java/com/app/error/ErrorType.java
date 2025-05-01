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
    FORBIDDEN("A-009", "접근 권한이 없습니다."),
    NOT_EXISTS_AUTH_MEMBER("A-010", "인증된 회원이 아닙니다."),
    NOT_ADMIN_MEMBER("A-011", "관리자 권한을 가진 회원만 API 호출이 가능합니다."),
    NOT_SUPER_ADMIN_MEMBER("A-012", "슈퍼 관리자 권한을 가진 회원만 API 호출이 가능합니다."),
    MEMBER_STATUS_STOP("A-013", "현재 사용 중단된 계정입니다."),
    MEMBER_STATUS_DEACTIVATE("A-014", "현재 탈퇴 된 계정입니다."),

    // 회원 관련 에러
    MEMBER_NOT_EXISTS("M-003", "해당 회원은 존재하지 않습니다."),
    NOT_SAME_PASSWORD("M-004", "비밀번호가 같지 않습니다. 비밀번호를 확인해주세요"),
    NOT_VALID_PASSWORD("M-004", "비밀번호가 일치하지 않습니다."),
    ALREADY_DEACTIVATE("M-008", "이미 탈퇴된 회원입니다."),

    PRODUCT_OPTION_SELECT_ID_NULL("P-001", "선택 타입일 경우상품 옵션의 상세 옵션 ID는 필수입니다."),
    PRODUCT_OPTION_INPUT_DESCRIPTION_NULL("P-002", "입력 타입일 경우 상품 옵션의 설명은 필수입니다."),
    ANY_PRODUCT_OPTION_DETAIL_NOT_FOUND("P-003", "존재하지 않는 상품 상세 옵션이 있습니다."),
    PRODUCT_OPTION_DETAIL_NOT_FOUND("P-004", "존재하지 않는 상품 상세 옵션입니다."),
    PRODUCT_OPTION_NAME_NULL("P-005", "상품 옵션의 이름은 필수입니다."),
    PRODUCT_OPTION_DETAIL_NAME_DUPLICATE("P-006", "상품 옵션 상세 이름이 중복되었습니다."),
    PRODUCT_OPTION_MAX_COUNT("P-007", "상품 옵션은 최대 3개까지 등록 가능합니다."),
    PRODUCT_NOT_FOUND("P-008", "존재하지 않는 상품입니다."),
    PRODUCT_OPTION_TYPE_NULL("P-009", "상품 옵션의 타입은 필수입니다."),
    PRODUCT_OPTION_NOT_FOUND("P-010", "존재하지 않는 상품 옵션입니다."),
    PRODUCT_OPTION_DETAIL_USED("P-011", "해당 상품 옵션 상세는 사용중입니다."),
    // 기타 에러
    SERVER_ERROR("E-001", "알 수 없는 에러가 발생하였습니다. 잠시 후에 시도해주세요."),
    ILLEGAL_ERROR("E-002", "잘못된 인수가 발견되어 에러가 발생했습니다. 적절한 인수로 시도해주세요.")
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

