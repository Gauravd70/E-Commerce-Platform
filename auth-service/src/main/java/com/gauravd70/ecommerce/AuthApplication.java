package com.gauravd70.ecommerce;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gauravd70.ecommerce.dtos.Roles;
import com.gauravd70.ecommerce.dtos.UserEntity;
import com.gauravd70.ecommerce.properties.AdminProperties;
import com.gauravd70.ecommerce.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "com.gauravd70.*")
@EnableJpaRepositories(basePackages = "com.gauravd70.ecommerce.repositories")
@EnableConfigurationProperties
@Slf4j
public class AuthApplication {
    private final UserRepository userRepository;
    private final AdminProperties adminProperties;
    private final PasswordEncoder passwordEncoder;

    @EventListener(value = ApplicationStartedEvent.class)
    public void onApplicationStarted(ApplicationStartedEvent event) {
        log.info("Creating admin user");

        UserEntity userEntity = UserEntity.builder()
                                    .firstName(adminProperties.getUsername())
                                    .lastName(adminProperties.getUsername())
                                    .username(adminProperties.getUsername())
                                    .password(passwordEncoder.encode(adminProperties.getPassword()))
                                    .roles(List.of(Roles.ADMIN.name()))
                                    .build();

        userRepository.save(userEntity);

        log.info("Admin user created successfully");
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
