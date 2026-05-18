package com.gauravd70.ecommerce.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.gauravd70.commons.filters.JwtFilter;
import com.gauravd70.ecommerce.dtos.Roles;

@Configuration
public class SecurityConfiguration {
    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    @Bean
    public SecurityWebFilterChain getSecurityFilterChain(JwtFilter jwtFilter, ServerHttpSecurity serverHttpSecurity) {
        return serverHttpSecurity
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(auth -> auth
                .pathMatchers("/v1/login", "/v1/signup").permitAll()
                .pathMatchers("/v1/logout").hasAnyRole(Roles.USER.name(), Roles.ADMIN.name())
                .anyExchange().denyAll())
            .addFilterAt(jwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .build();
    }
}
