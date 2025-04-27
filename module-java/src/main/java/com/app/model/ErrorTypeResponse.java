package com.app.model;

import com.app.error.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorTypeResponse {
    private String errorType;
    private String errorMessage;

    public static List<ErrorTypeResponse> getErrorTypes() {
        return Arrays.stream(ErrorType.values())
                .map(errorTypes -> new ErrorTypeResponse(errorTypes.getErrorCode(), errorTypes.getErrorMessage()))
                .toList();
    }
}
