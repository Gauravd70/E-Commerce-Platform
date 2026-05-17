package com.gauravd70.ecommerce.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.SignUpRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
public class AuthControllerTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.36"));

    @Autowired
    WebTestClient webTestClient;
    
    ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("com.gauravd70.ecommerce.dataproviders.AuthControllerDataProvider#invalidLoginRequest")
    void givenLoginRequest_whenInvalid_thenReturn400BadRequest(LoginRequest loginRequest) throws Exception {
        webTestClient.post().uri("/v1/login").contentType(MediaType.APPLICATION_JSON).bodyValue(loginRequest)
            .exchange()
            .expectStatus().isBadRequest();
    }

    @Test
    void givenLoginRequest_whenNotSignedUp_thenReturn400BadRequest() throws JsonProcessingException {
        LoginRequest request = LoginRequest.builder().username("abcd123").password("abcd123").build();

        webTestClient.post().uri("/v1/login").contentType(MediaType.APPLICATION_JSON).bodyValue(request)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody().json(objectMapper.writeValueAsString(GenericResponse.builder().message("Incorrect username or passoword.").build()));
    }

    @Test
    void givenLoginRequest_whenIncorrectCredentials_thenReturn200OK() throws JsonProcessingException {
        SignUpRequest signUpRequest = SignUpRequest.builder().firstName("test").lastName("user").username("test@gmail.com").password("Abcd@123").confirmPassword("Abcd@123").build();

        webTestClient.post().uri("/v1/signup").contentType(MediaType.APPLICATION_JSON).bodyValue(signUpRequest);

        LoginRequest loginRequest = LoginRequest.builder().username("test@gmail.com").password("Abcd@1234").build();

        webTestClient.post().uri("/v1/login").contentType(MediaType.APPLICATION_JSON).bodyValue(loginRequest)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody().json(objectMapper.writeValueAsString(GenericResponse.builder().message("Incorrect username or passoword.").build()));
    }

    @Test
    void givenLoginRequest_whenSignedUp_thenReturn200OK() throws JsonProcessingException {
        SignUpRequest signUpRequest = SignUpRequest.builder().firstName("test").lastName("user").username("test@gmail.com").password("Abcd@123").confirmPassword("Abcd@123").build();

        webTestClient.post().uri("/v1/signup").contentType(MediaType.APPLICATION_JSON).bodyValue(signUpRequest).exchange().expectStatus().isOk();

        LoginRequest loginRequest = LoginRequest.builder().username("test@gmail.com").password("Abcd@123").build();

        webTestClient.post().uri("/v1/login").contentType(MediaType.APPLICATION_JSON).bodyValue(loginRequest)
            .exchange()
            .expectStatus().isOk()
            .expectCookie().exists("ACCESS_TOKEN")
            .expectCookie().exists("REFRESH_TOKEN");
    }
}
