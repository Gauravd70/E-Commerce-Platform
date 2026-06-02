package com.gauravd70.ecommerce.handlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

@Component
public class ProductActionsHandler {
    @Async("productActionsExecutor")
    public void onHandle(ProductActionsMessage message) {
        Thread.currentThread().setName(message.getAction()+"-"+message.getId());
    }
}
