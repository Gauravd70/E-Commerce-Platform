package com.gauravd70.ecommerce.handlers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.repositories.ProductCatalogMappingsRepository;

public class ProductDeletedHandlerTest extends BaseProductHandlerTest {
    @Autowired
    ProductCatalogMappingsRepository productCatalogMappingsRepository;

    @Autowired
    ProductCreatedHandler productCreatedHandler;

    @Autowired
    ProductDeletedHandler productDeletedHandler;

    @Test
    void givenProductActionsMessage_whenProductDeleted_thenRemoveFromCatalog() {
        ProductActionsMessage createMessage = getProductActionsMessage();

        productCreatedHandler.onHandleMessage(createMessage).join();

        Assertions.assertThat(productCatalogMappingsRepository.count()).isEqualTo(1);

        ProductActionsMessage deleteMessage = ProductActionsMessage.builder().id(createMessage.getId().toString()).action(ProductAction.DELETE.name()).build();

        productDeletedHandler.onHandleMessage(deleteMessage).join();

        Assertions.assertThat(productCatalogMappingsRepository.count()).isEqualTo(0);
    }
}
