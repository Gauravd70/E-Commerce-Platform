package com.gauravd70.ecommerce.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.messages.CategoryMessage;
import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductCreatedHandlerTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:8.3.2"));

    @Autowired
    ProductCreatedHandler productCreatedHandler;

    @Autowired
    CatalogsRepository catalogsRepository;

    @Test
    void givenProductActionsMessage_whenCreateProduct_thenCreateCanonicalProduct() {
        List<String> variantAttributes = List.of("storage", "ram", "processor", "display size");

        CategoryMessage categoryMessage = CategoryMessage.builder()
            .id(new ObjectId().toString())
            .name("Laptop")
            .variantAttributes(variantAttributes)
            .build();

        Map<String, String> attributes = new HashMap<>();

        attributes.put("storage", "1TB");
        attributes.put("ram", "48GB");
        attributes.put("processor", "M5");
        attributes.put("display size", "16 inches");

        ProductActionsMessage productActionsMessage = ProductActionsMessage
            .builder()
            .id(new ObjectId().toString())
            .brand("Apple")
            .model("MacBook Pro")
            .attributes(attributes)
            .category(categoryMessage)
            .action(ProductAction.CREATE.name())
            .createdAt(System.currentTimeMillis())
            .version(1)
            .build();

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

        catalogsRepository.findAll().stream().findFirst()
            .ifPresentOrElse(
                t -> {
                    Assertions.assertThat(t.getId()).isNotNull();
                    Assertions.assertThat(t.getFamilyId()).isNotNull();
                    Assertions.assertThat(t.getVariantId()).isNotNull();
                    Assertions.assertThat(t.getCreatedAt()).isNotNull();
                    Assertions.assertThat(t.getUpdatedAt()).isNotNull();

                    Assertions.assertThat(t)
                        .usingRecursiveComparison()
                        .ignoringFields("id", "familyId", "variantId", "createdAt", "updatedAt")
                        .isEqualTo(expectedDocument);
                }, 
                () -> Assertions.fail());
    }
}
