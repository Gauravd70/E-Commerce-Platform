package com.gauravd70.ecommerce.services;

import org.springframework.http.ResponseEntity;
import com.gauravd70.ecommerce.dtos.LoginRequest;

import reactor.core.publisher.Mono;

public interface LoginService {
    /**
     * Logs in the user and return the session cookie
     * 
     * @param request {@LoginRequest}
     * @return {@Mono}
     */
    public Mono<ResponseEntity<Void>> onLogin(LoginRequest request);
}
