package com.gauravd70.ecommerce.services;

import org.springframework.http.ResponseEntity;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.commons.exceptions.UnauthorizedException;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.SignUpRequest;

public interface AuthService {
    /**
     * Logs in the user and return the session cookie
     * 
     * @param request {@link LoginRequest}
     * @return {@link ResponseEntity}<{@link Void}>
     * @throws UnauthorizedException
     */
    public ResponseEntity<Void> onLogin(LoginRequest request) throws UnauthorizedException;

    /**
     * Logs out the user
     * 
     * @param userId {@link String}
     * @return {@link ResponseEntity}<{@link Void}>
     * @throws UnauthorizedException
     */
    public ResponseEntity<Void> onLogout(String userId) throws UnauthorizedException;

    /**
     * Sign up a new user or seller
     * 
     * @param request request {@link SignUpRequest}
     * @return {@link GenericResponse}
     * @throws BadRequestException
     */
    public GenericResponse onSignUp(SignUpRequest request) throws BadRequestException;
}
