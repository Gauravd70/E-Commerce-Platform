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

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {InternalServerException.class, Exception.class})
    public ResponseEntity<GenericResponse> handleInternalServerException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(GenericResponse.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<GenericResponse> handleBadRequestException(BadRequestException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(GenericResponse.builder().message(e.getMessage()).build());
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    public ResponseEntity<GenericResponse> handleUnauthorizedException(UnauthorizedException e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GenericResponse.builder().message(e.getMessage()).build());
    }
}
