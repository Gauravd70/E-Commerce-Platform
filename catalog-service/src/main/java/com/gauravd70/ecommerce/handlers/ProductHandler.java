package com.gauravd70.ecommerce.handlers;

import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

public abstract class ProductHandler {
    protected abstract String getAction();

    public void onHandleMessage(ProductActionsMessage message) {
        Thread.currentThread().setName(getAction()+"-"+message.getId());
    }
}
