package com.gauravd70.ecommerce.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfigurations {
    @Bean
    public SecurityFilterChain configureSecurityFilterChain(HttpSecurity httpSecurity) {
        return httpSecurity.csrf(CsrfConfigurer::disable)
            .formLogin(FormLoginConfigurer::disable)
            .logout(LogoutConfigurer::disable)
            .httpBasic(HttpBasicConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.requestMatchers("/v1/**").permitAll().anyRequest().denyAll())
            .build();
    }
}
