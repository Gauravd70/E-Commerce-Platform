package com.gauravd70.commons.exceptions;

public class BadRequestException extends Exception{
    public BadRequestException() {
        super("Bad Request");
    }

    public BadRequestException(String message) {
        super(message);
    }
}
