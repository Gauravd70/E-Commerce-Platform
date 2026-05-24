package com.gauravd70.ecommerce.filters;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gauravd70.ecommerce.dtos.RoleEntity;
import com.gauravd70.ecommerce.repositories.RolesRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RolesRepository rolesRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Cookie accessCookie = getCookie(request.getCookies(), JwtType.ACCESS_TOKEN.name());

        if(accessCookie == null) {
            filterChain.doFilter(request, response);
            
            return;
        }

         Claims claims;

        try {
            claims = jwtUtils.getClaims(accessCookie.getValue());

            request.setAttribute(JwtType.ACCESS_TOKEN.name(), claims);
        } catch(Exception e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            return;
        }

        Optional<RoleEntity> roleEntityOptional = rolesRepository.findByName(claims.get("role", String.class));

        if(roleEntityOptional.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            return;
        }
        
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleEntityOptional.get().getName()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
        
    }

    private Cookie getCookie(Cookie[] cookies, String key) {
        if(cookies == null || key == null) {
            return null;
        }

        for(Cookie cookie : cookies) {
            if(cookie.getName().equals(key)) {
                return cookie;
            }
        }

        return null;
    }
}
