package com.gauravd70.ecommerce.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import com.gauravd70.commons.filters.JwtType;
import com.gauravd70.commons.filters.JwtUtils;
import com.gauravd70.ecommerce.base.BaseTest;

import jakarta.servlet.http.Cookie;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
public class BaseControllerTest extends BaseTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:8.3.2"));

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RabbitTemplate rabbitTemplate;

    Cookie getAccessToken(String role) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role);

        ResponseCookie cookie = jwtUtils.createCookie(JwtType.ACCESS_TOKEN, "1234", claims);

        return new Cookie(JwtType.ACCESS_TOKEN.name(), cookie.getValue());
    }

    MultiValueMap<String, String> toMultiValueMap(Object object) {
        Map<String, Object> map = objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});

        MultiValueMap<String, String> resultMap = new LinkedMultiValueMap<>();

        map.forEach((k, v) -> {
            if(v != null) {
                resultMap.add(k, v.toString());
            }
        });

        return resultMap;
    }
}
