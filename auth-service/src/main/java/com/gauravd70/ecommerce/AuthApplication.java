package com.gauravd70.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gauravd70.ecommerce.dtos.RoleEntity;
import com.gauravd70.ecommerce.dtos.UserEntity;
import com.gauravd70.ecommerce.dtos.UserRoleMappingEntity;
import com.gauravd70.ecommerce.dtos.UserRoleMappingId;
import com.gauravd70.ecommerce.properties.AdminProperties;
import com.gauravd70.ecommerce.repositories.RolesRepository;
import com.gauravd70.ecommerce.repositories.UserRoleMappingsRepository;
import com.gauravd70.ecommerce.repositories.UsersRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@SpringBootApplication(scanBasePackages = "com.gauravd70.*")
@EnableJpaRepositories(basePackages = "com.gauravd70.ecommerce.repositories")
@EnableConfigurationProperties
@Slf4j
public class AuthApplication {
    private final UsersRepository userRepository;
    private final RolesRepository rolesRepository;
    private final UserRoleMappingsRepository userRoleMappingsRepository;
    private final AdminProperties adminProperties;
    private final PasswordEncoder passwordEncoder;

    @EventListener(value = ApplicationStartedEvent.class)
    public void onApplicationStarted(ApplicationStartedEvent event) {
        UserEntity userEntity = UserEntity.builder()
                                    .firstname(adminProperties.getUsername())
                                    .lastname(adminProperties.getUsername())
                                    .username(adminProperties.getUsername())
                                    .password(passwordEncoder.encode(adminProperties.getPassword()))
                                    .build();
        
        try {
            userEntity = userRepository.save(userEntity);

            RoleEntity adminRole = rolesRepository.findByName("ROLE_ADMIN").get();

            UserRoleMappingEntity userRoleMappingEntity = UserRoleMappingEntity.builder().id(UserRoleMappingId.builder().userId(userEntity.getId()).roleId(adminRole.getId()).build()).build();

            userRoleMappingsRepository.save(userRoleMappingEntity);            

            log.info("Admin user created successfully");
        } catch(DataIntegrityViolationException e) {
            log.info("Admin user already exists!");
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
