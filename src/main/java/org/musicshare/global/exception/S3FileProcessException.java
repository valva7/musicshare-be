package org.musicshare.global.exception;

import lombok.Getter;

@Getter
public class S3FileProcessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final String message;

    public S3FileProcessException(String message) {
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        this.message = message;
    }
}
