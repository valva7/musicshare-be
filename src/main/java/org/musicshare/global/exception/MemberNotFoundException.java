package org.musicshare.global.exception;

public class MemberNotFoundException extends RuntimeException{

    private ErrorCode errorCode;

    public MemberNotFoundException() {
        this.errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    }

}
