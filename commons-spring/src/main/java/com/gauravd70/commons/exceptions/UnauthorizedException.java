package com.gauravd70.commons.exceptions;

public class UnauthorizedException extends Exception{
    public UnauthorizedException() {
        super("Unauthorized");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
