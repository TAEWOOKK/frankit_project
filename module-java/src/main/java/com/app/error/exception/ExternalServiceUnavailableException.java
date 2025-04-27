package com.app.error.exception;

import com.app.error.ErrorType;
import lombok.Getter;

/**
 * 외부 통신과 원활하지 않을 경우 발생하는 예외 클래스입니다.
 * 이 클래스는 외부와의 통신 과정에서 문제가 생긴 경우 던져지며, 비즈니스 로직에서 사용됩니다.
 *
 * 503 에러를 의미합니다.
 */
@Getter
public class ExternalServiceUnavailableException extends RuntimeException {

    // 발생한 비즈니스 예외의 유형을 나타내는 ErrorType
    private ErrorType errorType;

    /**
     * 주어진 ErrorType을 사용하여 ExternalServiceUnavailableException을 생성합니다.
     *
     * @param errorType 발생한 예외의 유형을 나타내는 ErrorType
     */
    public ExternalServiceUnavailableException(ErrorType errorType) {
        // RuntimeException의 생성자에 에러 메시지를 전달하여 초기화
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

    public ExternalServiceUnavailableException(ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorType = errorType;
    }

    public ExternalServiceUnavailableException(ErrorType errorType, Throwable e) {
        super(e);
        this.errorType = errorType;
    }

}
