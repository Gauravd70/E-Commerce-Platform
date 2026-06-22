package com.gauravd70.ecommerce.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gauravd70.commons.filters.JwtType;
import com.gauravd70.commons.filters.JwtUtils;
import com.gauravd70.ecommerce.base.BaseTest;

import jakarta.servlet.http.Cookie;
import tools.jackson.databind.ObjectMapper;

public class BaseControllerTest extends BaseTest {
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
}
