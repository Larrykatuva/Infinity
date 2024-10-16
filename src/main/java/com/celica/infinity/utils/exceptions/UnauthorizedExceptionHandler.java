package com.celica.infinity.utils.exceptions;

import com.celica.infinity.utils.dtos.ExceptionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UnauthorizedExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(UnauthorizedExceptionHandler.class);

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ExceptionResponseDto handleUnauthorizedException(UnauthorizedException exception) {
        Exception e = exception.getException();
        //logger.error(exception.getMessage(), e == null? exception: e);
        return new ExceptionResponseDto("Unauthorized", exception.getMessage());
    }
}
