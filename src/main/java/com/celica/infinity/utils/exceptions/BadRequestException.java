package com.celica.infinity.utils.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException {
    private final String title, message;

    @Override
    public String getMessage() {
        return message;
    }
}
