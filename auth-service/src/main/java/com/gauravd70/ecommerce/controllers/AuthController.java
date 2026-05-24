package com.gauravd70.ecommerce.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.commons.exceptions.UnauthorizedException;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.SignUpRequest;
import com.gauravd70.ecommerce.services.AuthService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class AuthController {
    private final AuthService authService;

    @PostMapping(value = "/login", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Void> onLogin(@Valid @RequestBody LoginRequest loginRequest) throws UnauthorizedException {
        return authService.onLogin(loginRequest);
    }

    @PostMapping(value = "/logout", produces = {MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<Void> onLogout(@RequestAttribute(value = "ACCESS_TOKEN") Claims accessTokenClaims) throws UnauthorizedException {
        return authService.onLogout(accessTokenClaims.getSubject());
    }

    @PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public GenericResponse onSignUpUser(@Valid @RequestBody SignUpRequest signUpRequest) throws BadRequestException {
        return authService.onSignUp(signUpRequest);
    }
}
