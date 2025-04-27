package com.app;

import com.app.error.ErrorResponse;
import com.app.error.ErrorType;
import com.app.error.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * javax.validation.Valid 또는 @Validated binding error가 발생할 경우
     * 400 반환
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error("handleBindException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), createErrorMessage(e));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    private String createErrorMessage(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            if(!isFirst) {
                sb.append(", ");
            } else {
                isFirst = false;
            }
            sb.append("[");
            sb.append(fieldError.getField());
            sb.append("] ");
            sb.append(fieldError.getDefaultMessage());
        }

        return sb.toString();
    }


    /**
     * 주로 @RequestParam enum으로 binding 못했을 경우 발생
     * 400 반환
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("handleMethodArgumentTypeMismatchException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * 비즈니스 로직 실행 중 오류 발생
     * 400 반환
     */
    @ExceptionHandler(value = { BadRequestException.class })
    protected ResponseEntity<ErrorResponse> handleConflict(BadRequestException e) {
        log.error("BadRequestException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorType().getErrorCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 자바빈 validation 처리
     * 400 반환
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(MethodArgumentNotValidException e, HttpServletRequest request) {
        log.error(e.getMessage(), e);
        StringBuilder sb = new StringBuilder();
        e.getBindingResult().getAllErrors()
                .forEach(c -> {
                    String errorMessage = getMessageSourceErrorMessage(c);
                    sb.append(errorMessage).append(" ");
                });
        String message = sb.toString();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), message));
    }

    /**
     * messages.properties에서 관리되고 있는 오류 메세지 조회
     */
    private String getMessageSourceErrorMessage(ObjectError error) {
        String[] codes = error.getCodes();
        for (String code : codes) {
            try {
                String message = messageSource.getMessage(code, error.getArguments(), Locale.KOREA);
                return message;
            } catch (NoSuchMessageException ignored) {}
        }
        return error.getDefaultMessage();
    }

    /**
     * 필드 TypeMismatch 오류 처리
     * 400 반환
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchExceptions(HttpMessageNotReadableException ex, HttpServletRequest request){
        log.error(ex.getMessage(), ex);
        Pattern errorFieldPattern = Pattern.compile("\\[[\"](.*?)[\"]\\]");
        Matcher errorFieldMatcher = errorFieldPattern.matcher(ex.getCause().getMessage());
        String errorField = errorFieldMatcher.find() ? errorFieldMatcher.group(1) : "FAIL";

        Pattern rightTypePattern = Pattern.compile("[`](.*?)[`]");
        Matcher rightTypeMatcher = rightTypePattern.matcher(ex.getMessage());
        String rightType = rightTypeMatcher.find() ? rightTypeMatcher.group(1) : "?";
        String errorMessage = messageSource.getMessage("typeMismatch", new Object[] {errorField, rightType}, Locale.KOREA);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), errorMessage));
    }

    /**
     * 필수 Request Param 넘기지 않았을 경우 오류 메세지 반환
     * 400 반환
     */
    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    protected ResponseEntity<ErrorResponse> handleConflict(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 잘못된 인수가 들어왔을 때 예외 처리
     * 400 반환
     */
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("IllegalArgumentException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorType.ILLEGAL_ERROR.getErrorCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 엔티티가 존재하지 않는 경우
     * 400 반환
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundExceptionn(EntityNotFoundException e) {
        log.error("EntityNotFoundException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorType());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 인증 실패 오류 처리
     * 401 반환
     */
    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<ErrorResponse> handleException(UnauthorizedException e) {
        log.error("UnauthorizedException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorType());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 스프링 시큐리티로 권한 검사 시 접근 권한이 없을 경우 처리
     * 403 반환
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleException(AccessDeniedException e) {
        log.error("AccessDeniedException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorType.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

     /**
     * 접근 권한이 없는 요청에 대한 예외 처리
     * 403 반환
     */
    @ExceptionHandler(ForbiddenException.class)
    protected ResponseEntity<ErrorResponse> handleException(ForbiddenException e) {
        log.error("ForbiddenException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorType.FORBIDDEN);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * 지원하지 않은 HTTP method 호출 할 경우 발생
     * 405 반환
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.METHOD_NOT_ALLOWED.toString(), e.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorResponse);
    }

    /**
     * 중복된 데이터나 요청이 있을 경우 예외 처리
     * 409 반환
     */
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<ErrorResponse> handleException(ConflictException e) {
        log.error("ConflictException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorType());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Content-type과 분법은 정확하지만 서버에서 명령을 처리할 수 없는 요청에 대한 예외 처리
     * 422 반환
     */
    @ExceptionHandler(UnprocessableEntityException.class)
    protected ResponseEntity<ErrorResponse> handleException(UnprocessableEntityException e) {
        log.error("UnprocessableEntityException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorType());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
    }

    /**
     * 외부 통신 오류에 대한 예외 발생
     * 503 반환
     */
    @ExceptionHandler(ExternalServiceUnavailableException.class)
    protected ResponseEntity<ErrorResponse> handleException(ExternalServiceUnavailableException e) {
        log.error("ExternalServiceUnavailableException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorType());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    /**
     * 서버 예외 발생
     * 500 반환
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception", e);

        if (e.getCause() instanceof IllegalArgumentException) {
            ErrorResponse errorResponse = ErrorResponse.of(ErrorType.ILLEGAL_ERROR.getErrorCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        ErrorResponse errorResponse = ErrorResponse.of(ErrorType.SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}