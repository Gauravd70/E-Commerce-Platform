package com.gauravd70.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.gauravd70.*"})
@EnableMongoRepositories(basePackages = {"com.gauravd70.ecommerce.repositories"})
public class CanonicalizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(CanonicalizationApplication.class, args);
    }
}
