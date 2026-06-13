package com.gauravd70.ecommerce.consumers;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.configurations.RabbitMQConfigurations;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.handlers.ProductHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductActionsConsumer {
    private final Map<String, ProductHandler> handlers;
    
    @RabbitListener(queues = {RabbitMQConfigurations.PRODUCT_ACTIONS_QUEUE})
    public void onHandleMessage(ProductActionsMessage message) {
        if(handlers.containsKey(message.getAction())) {
            handlers.get(message.getAction()).onHandleMessage(message);
        } else {
            log.info("No handlers found for action {}", message.getAction());
        }
    }
}
