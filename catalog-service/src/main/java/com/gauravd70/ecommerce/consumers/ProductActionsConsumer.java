package com.gauravd70.ecommerce.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.configurations.RabbitMQConfigurations;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.handlers.ProductActionsHandler;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductActionsConsumer {
    private ProductActionsHandler productActionsHandler;
    
    @RabbitListener(queues = {RabbitMQConfigurations.PRODUCT_ACTIONS_QUEUE})
    public void onHandleMessage(ProductActionsMessage message) {
        productActionsHandler.onHandle(message);
    }
}
