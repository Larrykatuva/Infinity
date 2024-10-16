package com.celica.infinity.utils.exceptions;

import com.celica.infinity.utils.dtos.ExceptionResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InternalServerErrorExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(InternalServerErrorExceptionHandler.class);

    private final BadRequestExceptionHandler badRequestExceptionHandler;

    private final UnauthorizedExceptionHandler unauthorizedExceptionHandler;

    public InternalServerErrorExceptionHandler(
            BadRequestExceptionHandler badRequestExceptionHandler,
            UnauthorizedExceptionHandler unauthorizedExceptionHandler
    ) {
        this.badRequestExceptionHandler = badRequestExceptionHandler;
        this.unauthorizedExceptionHandler = unauthorizedExceptionHandler;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InternalServerErrorException.class)
    public ExceptionResponseDto handleInternalServerError(InternalServerErrorException exception) {
        Exception e = exception.getException();
        if (e instanceof BadRequestException) {
            return badRequestExceptionHandler.handleBadRequest((BadRequestException) e);
        } else if (e instanceof UnauthorizedException) {
            return unauthorizedExceptionHandler.handleUnauthorizedException((UnauthorizedException) e);
        } else {
            logger.error(exception.getMessage(), e);
            return new ExceptionResponseDto(exception.getTitle(), exception.getMessage());
        }
    }
}
