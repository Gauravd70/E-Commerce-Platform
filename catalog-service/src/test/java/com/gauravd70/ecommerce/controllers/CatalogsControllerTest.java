package com.gauravd70.ecommerce.controllers;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gauravd70.ecommerce.controllers.dataproviders.CatalogsControllerDataProvider;
import com.gauravd70.ecommerce.controllers.dataproviders.testcases.GetCatalogsRequestTestCase;
import com.gauravd70.ecommerce.controllers.dataproviders.testcases.GetProductIdsRequestTestCase;
import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;
import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;
import com.gauravd70.ecommerce.dtos.requests.GetProductIdsRequest;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogDetails;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogsResponse;
import com.gauravd70.ecommerce.dtos.responses.GetProductIdsResponse;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;
import com.gauravd70.ecommerce.repositories.ProductCatalogMappingsRepository;

@TestInstance(Lifecycle.PER_CLASS)
public class CatalogsControllerTest extends BaseControllerTest {
    @Autowired
    CatalogsRepository catalogsRepository;

    @Autowired
    ProductCatalogMappingsRepository productCatalogMappingsRepository;

    @BeforeAll
    void onBeforeAll() {
        catalogsRepository.saveAll(CatalogsControllerDataProvider.catalogs);
        System.out.println(CatalogDocument.class.getName() + " added : " + catalogsRepository.count()); 

        productCatalogMappingsRepository.saveAll(CatalogsControllerDataProvider.productCatalogMappings);
        System.out.println(ProductCatalogMappingDocument.class.getName() + " added : " + productCatalogMappingsRepository.count());
    }
    
    @AfterAll
    void onAfterAll() {
        catalogsRepository.deleteAll();
        productCatalogMappingsRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("com.gauravd70.ecommerce.controllers.dataproviders.CatalogsControllerDataProvider#getCatalogRequestTestCases")
    void givenGetCatalogsRequest_whenValid_thenReturnCatalogList(GetCatalogsRequestTestCase testCase) throws Exception {
        String categoryId = testCase.getCategoryId().toString();
        GetCatalogsRequest request = testCase.getRequest();

        String response = mockMvc.perform(
            MockMvcRequestBuilders
                .get("/v1/"+categoryId)
                .queryParams(toMultiValueMap(request))
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        GetCatalogsResponse actualGetCatalogResponse = objectMapper.readValue(response, GetCatalogsResponse.class);

        List<CatalogDocument> catalogDocuments;
        
        if(request.getLastOffset() == null) {
            catalogDocuments = catalogsRepository.findFirst20ByCategoryId(categoryId);
        } else {
            catalogDocuments = catalogsRepository.findFirst20ByCategoryIdAndIdGreaterThan(categoryId.toString(), new ObjectId(request.getLastOffset()));
        }

        String lastOffset = null;

        if(catalogDocuments.size() > 0) {
            lastOffset = catalogDocuments.get(catalogDocuments.size() - 1).getId().toString();
        }
        
        List<GetCatalogDetails> catalogDetails = catalogDocuments.stream().map(document -> {
            return GetCatalogDetails
                .builder()
                .name(document.getName())
                .familyId(document.getFamilyId())
                .variantId(document.getVariantId())
                .attributes(document.getAttributes())
                .build();
        }).toList();

        GetCatalogsResponse expectedCatalogsResponse = GetCatalogsResponse.builder().catalogs(catalogDetails).lastOffset(lastOffset).build();

        Assertions.assertThat(actualGetCatalogResponse).isEqualTo(expectedCatalogsResponse);
    }

    @ParameterizedTest
    @MethodSource("com.gauravd70.ecommerce.controllers.dataproviders.CatalogsControllerDataProvider#getProductIdsRequestTestCases")
    void givenGetProductIdsRequest_whenValid_thenReturnProductIdsList(GetProductIdsRequestTestCase testCase) throws Exception {
        GetProductIdsRequest request = testCase.getRequest();

        String response = mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/v1/ids/"+testCase.getFamilyId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParams(toMultiValueMap(request))
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

        GetProductIdsResponse actualResponse = objectMapper.readValue(response, GetProductIdsResponse.class);

        List<ProductCatalogMappingDocument> mappings;

        if(request.getLastOffset() == null) {
            mappings = productCatalogMappingsRepository.findFirst20ByFamilyIdAndVariantId(testCase.getFamilyId(), testCase.getVariantId());
        } else {
            mappings = productCatalogMappingsRepository.findFirst20ByFamilyIdAndVariantIdAndIdGreaterThan(testCase.getFamilyId(), testCase.getVariantId(), new ObjectId(request.getLastOffset()));
        }

        List<String> productIds = mappings.stream().map(document -> document.getProductId()).toList();

        String lastOffset = null;

        if(mappings.size() > 0) {
            lastOffset = mappings.get(mappings.size() - 1).getId().toString();
        }

        GetProductIdsResponse expectedResponse = GetProductIdsResponse.builder().products(productIds).lastOffset(lastOffset).build();

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
