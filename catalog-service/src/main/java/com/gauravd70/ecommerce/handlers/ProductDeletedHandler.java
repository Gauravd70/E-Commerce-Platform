package com.gauravd70.ecommerce.handlers;

import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.repositories.ProductCatalogMappingsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductDeletedHandler extends ProductHandler {
    private final ProductCatalogMappingsRepository productCatalogMappingsRepository;

    @Override
    protected String getAction() {
        return ProductAction.DELETE.name();
    }

    @Override
    public CompletableFuture<Void> onHandleMessage(ProductActionsMessage message) {
        super.onHandleMessage(message);

        productCatalogMappingsRepository.findOneByProductId(message.getId())
            .ifPresent(document -> productCatalogMappingsRepository.deleteOneByProductId(message.getId()));

        return CompletableFuture.completedFuture(null);
    }
}
