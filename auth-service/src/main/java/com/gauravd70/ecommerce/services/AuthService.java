package com.gauravd70.ecommerce.services;

import org.springframework.http.ResponseEntity;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.Roles;
import com.gauravd70.ecommerce.dtos.SignUpRequest;

import reactor.core.publisher.Mono;

public interface AuthService {
    /**
     * Logs in the user and return the session cookie
     * 
     * @param request {@link LoginRequest}
     * @return {@link Mono}<{@link ResponseEntity}>
     */
    public Mono<ResponseEntity<Void>> onLogin(LoginRequest request);

    /**
     * Logs out the user
     * 
     * @param userId {@link String}
     * @return {@link Mono}<{@link ResponseEntity}>
     */
    public Mono<ResponseEntity<Void>> onLogout(String userId);

    /**
     * Sign up a new user or seller
     * 
     * @param request request {@link SignUpRequest}
     * @param role {@link Roles}
     * @return {@link Mono}<{@link GenericResponse}>
     */
    public Mono<GenericResponse> onSignUp(SignUpRequest request, Roles role);
}
