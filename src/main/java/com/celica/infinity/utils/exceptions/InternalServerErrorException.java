package com.celica.infinity.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InternalServerErrorException extends RuntimeException {
    private final String title, message;
    private final Exception exception;

    @Override
    public String getMessage() {
        return message;
    }
}
