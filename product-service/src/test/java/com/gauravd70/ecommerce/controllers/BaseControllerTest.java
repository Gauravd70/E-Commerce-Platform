package com.gauravd70.ecommerce.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;

import com.gauravd70.commons.filters.JwtType;
import com.gauravd70.commons.filters.JwtUtils;

import jakarta.servlet.http.Cookie;
import tools.jackson.databind.ObjectMapper;

public class BaseControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ObjectMapper objectMapper;

    Cookie getAccessToken(String role) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("role", role);

        ResponseCookie cookie = jwtUtils.createCookie(JwtType.ACCESS_TOKEN, "1234", claims);

        return new Cookie(JwtType.ACCESS_TOKEN.name(), cookie.getValue());
    }
}
