package com.gauravd70.commons;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.commons.exceptions.InternalServerException;

import reactor.core.publisher.Mono;

@ControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {InternalServerException.class, Exception.class})
    public Mono<ResponseEntity<GenericResponse>> handleInternalServerException(InternalServerException e) {
        return Mono.just(ResponseEntity.internalServerError().body(GenericResponse.builder().message(e.getMessage()).build()));
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public Mono<ResponseEntity<GenericResponse>> handleBadRequestException(BadRequestException e) {
        return Mono.just(ResponseEntity.badRequest().body(GenericResponse.builder().message(e.getMessage()).build()));
    }
}
