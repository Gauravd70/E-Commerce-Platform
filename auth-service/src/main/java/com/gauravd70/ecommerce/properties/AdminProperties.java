package com.gauravd70.ecommerce.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(value = "admin")
@Getter
@Setter
public class AdminProperties {
    private String username;
    private String password;
}
