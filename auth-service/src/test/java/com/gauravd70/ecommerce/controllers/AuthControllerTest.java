package com.gauravd70.ecommerce.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.ecommerce.dtos.LoginRequest;
import com.gauravd70.ecommerce.dtos.SignUpRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Testcontainers
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.0.36"));

    @Autowired
    MockMvc mockMvc;
    
    ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @MethodSource("com.gauravd70.ecommerce.dataproviders.AuthControllerDataProvider#invalidLoginRequest")
    void givenLoginRequest_whenInvalid_thenReturn400BadRequest(LoginRequest loginRequest) throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenLoginRequest_whenNotSignedUp_thenReturn401Unauthorized() throws Exception {
        LoginRequest request = LoginRequest.builder().username("abcd123").password("abcd123").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andReturn()
            .getResponse()
            .getContentAsString().equals(objectMapper.writeValueAsString(GenericResponse.builder().message("Incorrect username or passoword.").build()));
    }

    @Test
    void givenLoginRequest_whenIncorrectCredentials_thenReturn401Unauthorized() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder().firstname("test").lastname("user").username("test@gmail.com").password("Abcd@123").confirmPassword("Abcd@123").role("ROLE_CUSTOMER").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpRequest)));

        LoginRequest loginRequest = LoginRequest.builder().username("test@gmail.com").password("Abcd@1234").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized())
            .andReturn()
            .getResponse()
            .getContentAsString().equals(objectMapper.writeValueAsString(GenericResponse.builder().message("Incorrect username or passoword.").build()));
    }

    @Test
    void givenLoginRequest_whenSignedUp_thenReturn200OK() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder().firstname("test").lastname("user").username("test2@gmail.com").password("Abcd@123").confirmPassword("Abcd@123").role("ROLE_SELLER").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk());

        LoginRequest loginRequest = LoginRequest.builder().username("test2@gmail.com").password("Abcd@123").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.cookie().exists("ACCESS_TOKEN"))
            .andExpect(MockMvcResultMatchers.cookie().exists("REFRESH_TOKEN"));
    }

    @Test
    void givenSignUpRequest_whenEmailExists_thenReturn400BadRequest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder().firstname("test").lastname("user").username("test3@gmail.com").password("Abcd@123").confirmPassword("Abcd@123").role("ROLE_CUSTOMER").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString().equals(objectMapper.writeValueAsString(GenericResponse.builder().message("Username already exists").build()));
    }

    @Test
    void givenSignUpRequest_whenPasswordMismatch_thenReturn400BadRequest() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder().firstname("test").lastname("user").username("test4@gmail.com").password("Abcd@123").confirmPassword("Abcd@1234").role("ROLE_CUSTOMER").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpRequest)))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString().equals(objectMapper.writeValueAsString(GenericResponse.builder().message("Passwords do not match").build()));
    }

     @Test
    void givenLogoutRequest_whenNoAccessToken_thenReturn401Unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/logout"))
            .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }


    @Test
    void givenLogoutRequest_whenSuccess_thenReturn200OK() throws Exception {
        SignUpRequest signUpRequest = SignUpRequest.builder().firstname("test").lastname("user").username("test4@gmail.com").password("Abcd@123").confirmPassword("Abcd@123").role("ROLE_CUSTOMER").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(signUpRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk());

        LoginRequest loginRequest = LoginRequest.builder().username("test4@gmail.com").password("Abcd@123").build();

        MvcResult mvcResult = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.cookie().exists("ACCESS_TOKEN"))
            .andExpect(MockMvcResultMatchers.cookie().exists("REFRESH_TOKEN"))
            .andReturn();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(mvcResult.getResponse().getCookie("ACCESS_TOKEN"))
                .content(objectMapper.writeValueAsBytes(loginRequest)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.cookie().exists("ACCESS_TOKEN"))
            .andExpect(MockMvcResultMatchers.cookie().value("ACCESS_TOKEN", ""))
            .andExpect(MockMvcResultMatchers.cookie().maxAge("ACCESS_TOKEN", 0))
            .andExpect(MockMvcResultMatchers.cookie().exists("REFRESH_TOKEN"))
            .andExpect(MockMvcResultMatchers.cookie().value("REFRESH_TOKEN", ""))
            .andExpect(MockMvcResultMatchers.cookie().maxAge("REFRESH_TOKEN", 0));
    }
}
