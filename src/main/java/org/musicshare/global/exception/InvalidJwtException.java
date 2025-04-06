package org.musicshare.global.exception;

import lombok.Getter;

@Getter
public class InvalidJwtException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public InvalidJwtException(String message) {
        this.errorCode = ErrorCode.UNAUTHORIZED;
        this.message = message;
    }
}