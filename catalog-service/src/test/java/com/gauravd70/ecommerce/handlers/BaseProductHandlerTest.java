package com.gauravd70.ecommerce.handlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import com.gauravd70.ecommerce.dtos.messages.CategoryMessage;
import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;
import com.gauravd70.ecommerce.repositories.ProductCatalogMappingsRepository;

public class BaseProductHandlerTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:8.3.2"));

    @Autowired
    CatalogsRepository catalogsRepository;

    @Autowired
    ProductCatalogMappingsRepository productCatalogMappingsRepository;

    ProductActionsMessage getProductActionsMessage() {
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

        return ProductActionsMessage
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
    }

    @AfterEach
    void onTestCompleted() {
        catalogsRepository.deleteAll();
        productCatalogMappingsRepository.deleteAll();
    }
}
