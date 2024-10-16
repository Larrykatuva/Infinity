package com.celica.infinity.utils.exceptions;

import com.celica.infinity.utils.dtos.ExceptionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class BadRequestExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(BadRequestExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public ExceptionResponseDto handleBadRequest(BadRequestException exception) {
        //logger.error(exception.getMessage(), exception);
        return new ExceptionResponseDto(exception.getTitle(), exception.getMessage());
    }
}
