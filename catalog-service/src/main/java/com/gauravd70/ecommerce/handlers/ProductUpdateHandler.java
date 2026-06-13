package com.gauravd70.ecommerce.handlers;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

@Component
public class ProductUpdateHandler extends ProductHandler {
    @Override
    protected String getAction() {
        return ProductAction.UPDATE.name();
    }

    @Async("productActionsExecutor")
    @Override
    public void onHandleMessage(ProductActionsMessage message) {
        super.onHandleMessage(message);
    }
}
