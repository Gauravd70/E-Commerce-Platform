package com.gauravd70.ecommerce.handlers;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;
import com.gauravd70.ecommerce.dtos.messages.CategoryMessage;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

public class ProductCreatedHandlerTest extends BaseProductHandlerTest {
    @Autowired
    ProductCreatedHandler productCreatedHandler;

    @Test
    void givenProductActionsMessage_whenCreateProduct_thenCreateCanonicalProduct() {
        ProductActionsMessage productActionsMessage = getProductActionsMessage();

        CategoryMessage categoryMessage = productActionsMessage.getCategory();

        Map<String, String> expectedAttributes = new HashMap<>();

        expectedAttributes.put("storage", "1tb");
        expectedAttributes.put("ram", "48gb");
        expectedAttributes.put("processor", "m5");
        expectedAttributes.put("display size", "16 inches");

        CatalogDocument expectedDocument = CatalogDocument
            .builder()
            .name("Apple Macbook Pro")
            .categoryId(categoryMessage.getId())
            .familyIdRepresentation("brand=apple|model=macbook pro")
            .variantIdRepresentation("display size=16 inches|processor=m5|ram=48gb|storage=1tb")
            .attributes(expectedAttributes)
            .build();

        productCreatedHandler.onHandleMessage(productActionsMessage).join();

        CatalogDocument actualDocument = catalogsRepository.findAll().stream().findFirst().orElseGet(() -> Assertions.fail());

        Assertions.assertThat(actualDocument.getId()).isNotNull();
        Assertions.assertThat(actualDocument.getFamilyId()).isNotNull();
        Assertions.assertThat(actualDocument.getVariantId()).isNotNull();
        Assertions.assertThat(actualDocument.getCreatedAt()).isNotNull();
        Assertions.assertThat(actualDocument.getUpdatedAt()).isNotNull();

        Assertions.assertThat(actualDocument)
            .usingRecursiveComparison()
            .ignoringFields("id", "familyId", "variantId", "createdAt", "updatedAt")
            .isEqualTo(expectedDocument);
        
        ProductCatalogMappingDocument expectedProductCatalogMappingDocument = ProductCatalogMappingDocument
                    .builder()
                    .productId(productActionsMessage.getId())
                    .familyId(actualDocument.getFamilyId())
                    .variantId(actualDocument.getVariantId())
                    .build();
        
        ProductCatalogMappingDocument actualProductCatalogMappingDocument = productCatalogMappingsRepository.findOneByProductId(productActionsMessage.getId()).orElseGet(() -> Assertions.fail());

        Assertions.assertThat(actualProductCatalogMappingDocument)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedProductCatalogMappingDocument);
    }

    @Test
    void givenProductActionsMessage_whenProductCreatedAndCanonicalProductAlreadyExists_thenCreateMapping() {
        ProductActionsMessage productActionsMessage1 = getProductActionsMessage();

        productCreatedHandler.onHandleMessage(productActionsMessage1).join();

        ProductActionsMessage productActionsMessage2 = getProductActionsMessage();

        productCreatedHandler.onHandleMessage(productActionsMessage2).join();

        Assertions.assertThat(catalogsRepository.count()).isEqualTo(1);

        CatalogDocument catalogDocument = catalogsRepository.findAll().stream().findFirst().orElseGet(() -> Assertions.fail());

        Assertions.assertThat(productCatalogMappingsRepository.findFirst20ByFamilyIdAndVariantId(catalogDocument.getFamilyId(), catalogDocument.getVariantId()).size()).isEqualTo(2);
    }
}
