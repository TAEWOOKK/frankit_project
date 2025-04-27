package com.app.error.exception;

import com.app.error.ErrorType;
import lombok.Getter;

/**
 * 프론트에서 보낸 요청의 Content-type과 문법은 정확하지만
 * 요청을 처리할 수 없는 경우 던져지는 예외 클래스입니다.
 *
 * 422 에러를 의미합니다.
 */
@Getter
public class UnprocessableEntityException extends RuntimeException {

    // 발생한 비즈니스 예외의 유형을 나타내는 ErrorType
    private ErrorType errorType;

    /**
     * 주어진 ErrorType을 사용하여 UnprocessableEntityException을 생성합니다.
     *
     * @param errorType 발생한 예외의 유형을 나타내는 ErrorType
     */
    public UnprocessableEntityException(ErrorType errorType) {
        // RuntimeException의 생성자에 에러 메시지를 전달하여 초기화
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }
}
