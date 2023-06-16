package com.event.postservice.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BussinessException extends RuntimeException{

    private String message;

    private final ErrorCode errorCode;

    @Override
    public String getMessage() {
        return this.message;
    }

    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

}
