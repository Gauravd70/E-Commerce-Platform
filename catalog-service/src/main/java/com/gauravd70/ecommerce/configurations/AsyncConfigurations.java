package com.gauravd70.ecommerce.configurations;

import java.util.concurrent.RejectedExecutionException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AsyncConfigurations {

    @Bean("productActionsExecutor")
    public ThreadPoolTaskExecutor getProductActionsTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setQueueCapacity(80);
        taskExecutor.setKeepAliveSeconds(10);
        taskExecutor.setThreadNamePrefix("product-");
        taskExecutor.setRejectedExecutionHandler((r, executor) -> {
            try {
                executor.getQueue().put(r);
            } catch(InterruptedException e) {
                throw new RejectedExecutionException(e);
            }
        });

        return taskExecutor;
    }
}
