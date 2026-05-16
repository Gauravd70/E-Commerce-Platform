package com.gauravd70.ecommerce.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.services.LoginService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/login")
public class LoginController {
    private LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<ResponseEntity<Void>> onLogin(@Valid LoginRequest loginRequest) {
        return loginService.onLogin(loginRequest);
    }
}
