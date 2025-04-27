package com.app.error.exception;

import com.app.error.ErrorType;
import lombok.Getter;

/**
 * 엔터티(데이터베이스 객체 등)를 찾을 수 없는 경우 발생하는 예외 클래스입니다.
 * 이 클래스는 특정 엔터티를 찾지 못했을 때 던져지며, 비즈니스 로직에서 사용됩니다.
 */
@Getter
public class EntityNotFoundException extends RuntimeException {

    // 발생한 비즈니스 예외의 유형을 나타내는 ErrorType
    private ErrorType errorType;

    /**
     * 주어진 ErrorType을 사용하여 EntityNotFoundException을 생성합니다.
     *
     * @param errorType 발생한 예외의 유형을 나타내는 ErrorType
     */
    public EntityNotFoundException(ErrorType errorType) {
        // RuntimeException의 생성자에 에러 메시지를 전달하여 초기화
        super(errorType.getErrorMessage());
        this.errorType = errorType;
    }

}

