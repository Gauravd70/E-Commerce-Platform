package com.gauravd70.ecommerce.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.SignUpRequest;
import com.gauravd70.ecommerce.services.AuthService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public Mono<ResponseEntity<Void>> onLogin(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.onLogin(loginRequest);
    }

    @PostMapping(value = "/logout", produces = {MediaType.TEXT_PLAIN_VALUE})
    public Mono<ResponseEntity<Void>> onLogout(@RequestAttribute(value = "ACCESS_TOKEN") Claims accessTokenClaims) {
        return authService.onLogout(accessTokenClaims.getSubject());
    }

    @PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<GenericResponse> onSignUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        return authService.onSignUp(signUpRequest);
    }
}
