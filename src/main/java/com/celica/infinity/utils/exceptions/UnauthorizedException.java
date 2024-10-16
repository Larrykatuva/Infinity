package com.celica.infinity.utils.exceptions;

import lombok.Getter;

public class UnauthorizedException extends RuntimeException {
    private final String message;

    @Getter
    private final Exception exception;

    public UnauthorizedException(String message) {
        this.message = message;
        this.exception = null;
    }

    public UnauthorizedException(String message, Exception exception) {
        this.message = message;
        this.exception = exception;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
