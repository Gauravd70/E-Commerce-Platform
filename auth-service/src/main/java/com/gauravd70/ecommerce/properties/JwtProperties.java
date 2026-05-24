package com.gauravd70.ecommerce.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(value = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private Expiry expiry;

    @Getter
    @Setter
    public static class Expiry {
        private long access = 3600000;
        private long refresh = 604800000;
    }
}
