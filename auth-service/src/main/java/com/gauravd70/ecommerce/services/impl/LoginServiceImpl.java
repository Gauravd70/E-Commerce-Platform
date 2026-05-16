package com.gauravd70.ecommerce.services.impl;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.repositories.UserRepository;
import com.gauravd70.ecommerce.services.LoginService;

import reactor.core.publisher.Mono;

@Service
public class LoginServiceImpl implements LoginService {
    private UserRepository userRepository;

    public LoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<ResponseEntity<Void>> onLogin(LoginRequest request) {
        return userRepository.findById(request.getUsername())
        .switchIfEmpty(Mono.error(new BadRequestException("Incorrect username or passoword.")))
        .map(userEntity -> ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, 
                                ResponseCookie.from("userId", String.valueOf(userEntity.getId()))
                                .httpOnly(true)
                                .path("/")
                                .maxAge(3600)
                                .build().toString()
                            ).build()
                        );
    }
}
