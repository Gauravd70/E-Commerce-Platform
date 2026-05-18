package com.gauravd70.commons.filters;

import java.util.List;

import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.gauravd70.commons.security.JwtType;
import com.gauravd70.commons.security.JwtUtils;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class JwtFilter implements WebFilter {
    private final JwtUtils jwtUtils;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();

        HttpCookie accessCookie = cookies.getFirst(JwtType.ACCESS_TOKEN.name());

        if(accessCookie == null) {
            return chain.filter(exchange);
        }

        Claims claims;

        try {
            claims = jwtUtils.getClaims(accessCookie.getValue());

            request.getAttributes().put(JwtType.ACCESS_TOKEN.name(), claims);
        } catch(Exception e) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);

            return response.setComplete();
        }
        
        List<GrantedAuthority> authorities = ((List<?>)claims.get("roles", List.class)).stream().map(object -> "ROLE_".concat(object.toString())).map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role)).toList();

        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

        SecurityContext securityContext = new SecurityContextImpl(authentication);

        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }
}
