package com.app.error.exception;

import com.app.error.ErrorType;
import lombok.Getter;

/**
 * 접근 권한 관련 오류가 발생할 때 던져지는 예외 클래스입니다.
 * 이 클래스는 권한을 접근하는 과정에서 문제가 발생했을 때 사용되며, 비즈니스 로직에서 사용됩니다.
 *
 * 403 에러를 의미합니다.
 */
@Getter
public class ForbiddenException extends RuntimeException {

    // 발생한 비즈니스 예외의 유형을 나타내는 ErrorType
    private ErrorType errorType;

    /**
     * 주어진 ErrorType을 사용하여 ForbiddenException을 생성합니다.
     *
     * @param errorType 발생한 예외의 유형을 나타내는 ErrorType
     */
    public ForbiddenException(ErrorType errorType) {
        // RuntimeException의 생성자에 에러 메시지를 전달하여 초기화
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

}
