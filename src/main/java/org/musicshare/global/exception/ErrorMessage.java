package org.musicshare.global.exception;

import lombok.Getter;

@Getter
public enum ErrorMessage {

    TEXT("TEXT");

    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}
