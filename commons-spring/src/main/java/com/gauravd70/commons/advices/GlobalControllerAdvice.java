package com.gauravd70.commons.advices;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.commons.exceptions.InternalServerException;
import com.gauravd70.commons.exceptions.UnauthorizedException;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {InternalServerException.class, Exception.class})
    public ResponseEntity<GenericResponse> handleInternalServerException(Exception e) {
        return ResponseEntity.internalServerError().body(GenericResponse.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<GenericResponse> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.badRequest().body(GenericResponse.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<GenericResponse> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponse.builder().message(e.getMessage()).build());
    }
}
