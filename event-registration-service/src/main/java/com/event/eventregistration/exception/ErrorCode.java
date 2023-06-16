package com.event.eventregistration.exception;

public enum ErrorCode {
    unknown(400),
    validation(422),
    unauthorized(401),
    forbidden(403),
    resource_missing(404),
    conflict(409);

    private final int httpCode;

    ErrorCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public int getHttpCode() {
        return httpCode;
    }
}
