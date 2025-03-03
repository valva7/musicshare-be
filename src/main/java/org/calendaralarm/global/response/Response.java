package org.calendaralarm.global.response;


import org.calendaralarm.global.exception.ErrorCode;

public record Response<T>(Integer code, String message, T value) {

    public static <T> Response<T> ok(T value){
        return new Response<>(0, "ok", value);
    }

    public static <T> Response<T> error(ErrorCode error){
        return new Response<>(error.getCode(), error.getMessage(), null);
    }

}
