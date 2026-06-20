package com.gauravd70.ecommerce.handlers;

import java.util.concurrent.CompletableFuture;

import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

public abstract class ProductHandler {
    protected abstract String getAction();

    public CompletableFuture<Void> onHandleMessage(ProductActionsMessage message) {
        Thread.currentThread().setName(getAction()+"-"+message.getId());

        return CompletableFuture.completedFuture(null);
    }
}
