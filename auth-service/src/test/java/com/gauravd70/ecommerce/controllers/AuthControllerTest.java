package com.gauravd70.ecommerce.controllers;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.ExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.gauravd70.ecommerce.dtos.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
@Testcontainers
public class AuthControllerTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.36"));

    @Autowired
    WebTestClient webTestClient;

    @ParameterizedTest
    @MethodSource("com.gauravd70.ecommerce.dataproviders.AuthControllerDataProvider#invalidLoginRequest")
    void givenLoginRequest_whenInvalid_thenReturn400BadRequest(LoginRequest loginRequest) throws Exception {
        ExchangeResult result = webTestClient.post().uri("/v1/login").contentType(MediaType.APPLICATION_JSON).bodyValue(loginRequest).exchange().expectStatus().isBadRequest().returnResult();
    }
}
