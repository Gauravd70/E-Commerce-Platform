package com.gauravd70.commons.filters;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.gauravd70.commons.security.JwtType;
import com.gauravd70.commons.security.JwtUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import reactor.core.publisher.Mono;

@Component
public class JwtFilter implements WebFilter {
    private JwtUtils jwtUtils;

    public JwtFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();

        HttpCookie accessCookie = cookies.getFirst(JwtType.ACCESS_TOKEN.name());

        if(accessCookie == null) {
            return chain.filter(exchange);
        }

        try {
            jwtUtils.getClaims(accessCookie.getValue());
        } catch(Exception e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);

            return response.setComplete();
        }

        return chain.filter(exchange);
    }


}
