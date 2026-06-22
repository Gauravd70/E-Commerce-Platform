package com.gauravd70.ecommerce.handlers;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;
import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

public class ProductUpdatedHandlerTest extends BaseProductHandlerTest {
    @Autowired
    ProductUpdateHandler productUpdateHandler;

    @Autowired
    ProductCreatedHandler productCreatedHandler;

    @Test
    void givenProductActionsMessage_whenUpdateProduct_thenUpdateCanonicalProduct() {
        ProductActionsMessage createMessage = getProductActionsMessage();

        productCreatedHandler.onHandleMessage(createMessage).join();

        ProductActionsMessage updateMessage = getProductActionsMessage();
        
        updateMessage.setId(createMessage.getId());
        updateMessage.setAction(ProductAction.UPDATE.name());
        updateMessage.setModel("MacBook Air");
        
        productUpdateHandler.onHandleMessage(updateMessage).join();

        CatalogDocument actualCatalogDocument = catalogsRepository.findAll().stream().filter(document -> document.getName().equals("Apple Macbook Air")).findFirst().orElseGet(() -> Assertions.fail());

        ProductCatalogMappingDocument expectedProductCatalogMappingDocument = ProductCatalogMappingDocument
            .builder()
            .productId(updateMessage.getId())
            .familyId(actualCatalogDocument.getFamilyId())
            .variantId(actualCatalogDocument.getVariantId())
            .build();

        Assertions.assertThat(catalogsRepository.count()).isEqualTo(2);
        Assertions.assertThat(productCatalogMappingsRepository.count()).isEqualTo(1);

        ProductCatalogMappingDocument actualProductCatalogMappingDocument = productCatalogMappingsRepository.findOneByProductId(updateMessage.getId()).orElseGet(() -> Assertions.fail());

        Assertions.assertThat(actualProductCatalogMappingDocument)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedProductCatalogMappingDocument);
    }
}
