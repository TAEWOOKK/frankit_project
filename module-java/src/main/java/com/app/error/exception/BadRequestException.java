package com.app.error.exception;

import com.app.error.ErrorType;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    // 발생한 비즈니스 예외의 유형을 나타내는 ErrorType
    private ErrorType errorType;

    /**
     * 주어진 ErrorType에 해당하는 메시지를 가진 BadRequestException을 생성합니다.
     * 400 에러를 의미합니다.
     *
     * @param errorType 발생한 비즈니스 예외의 유형을 나타내는 ErrorType
     */
    public BadRequestException(ErrorType errorType) {
        // RuntimeException의 생성자에 에러 메시지를 전달하여 초기화
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

    public BadRequestException(ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorType = errorType;
    }

    public BadRequestException(ErrorType errorType, Throwable e) {
        super(e);
        this.errorType = errorType;
    }

}

