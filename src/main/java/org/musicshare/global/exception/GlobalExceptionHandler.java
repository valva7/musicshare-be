package org.musicshare.global.exception;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.musicshare.global.response.Response;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public Response<Void> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(ErrorCode.INVALID_INPUT_VALUE, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Response<Void> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(ErrorCode.INVALID_INPUT_VALUE, Objects.requireNonNull(exception.getBindingResult().getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(Exception.class)
    public Response<Void> handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public Response<Void> memberNotFoundException(MemberNotFoundException exception) {
        log.error(exception.getMessage(), exception);
        return Response.error(ErrorCode.INTERNAL_SERVER_ERROR);
    }


}
