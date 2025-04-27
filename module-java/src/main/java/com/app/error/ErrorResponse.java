package com.app.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    // 에러 코드 (예: "NOT_FOUND", "UNAUTHORIZED" 등)
    private String errorCode;

    // 에러 메시지 (예: "리소스를 찾을 수 없습니다.", "인증이 필요합니다." 등)
    private String errorMessage;

    /**
     * 주어진 에러 코드와 에러 메시지를 사용하여 ErrorResponse 객체를 생성하는 정적 팩토리 메서드입니다.
     *
     * @param errorCode 에러 코드
     * @param errorMessage 에러 메시지
     * @return 생성된 ErrorResponse 객체
     */
    public static ErrorResponse of(String errorCode, String errorMessage) {
        return ErrorResponse.builder()
                .errorCode(errorCode)         // 에러 코드 설정
                .errorMessage(errorMessage)   // 에러 메시지 설정
                .build();                     // ErrorResponse 객체 생성
    }

    /**
     * 주어진 ErrorType을 사용하여 ErrorResponse 객체를 생성하는 정적 팩토리 메서드입니다.
     *
     * @param errorType ErrorType 열거형 (에러 코드와 메시지를 포함)
     * @return 생성된 ErrorResponse 객체
     */
    public static ErrorResponse of(ErrorType errorType) {
        return ErrorResponse.builder()
                .errorCode(errorType.getErrorCode())       // ErrorType에서 에러 코드 설정
                .errorMessage(errorType.getErrorMessage()) // ErrorType에서 에러 메시지 설정
                .build();                                  // ErrorResponse 객체 생성
    }

}

