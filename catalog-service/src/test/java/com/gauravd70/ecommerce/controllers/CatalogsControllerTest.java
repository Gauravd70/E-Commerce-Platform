package com.gauravd70.ecommerce.controllers;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.gauravd70.ecommerce.controllers.dataproviders.CatalogsControllerDataProvider;
import com.gauravd70.ecommerce.controllers.dataproviders.GetCatalogsRequestTestCase;
import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogDetails;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogsResponse;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;

@TestInstance(Lifecycle.PER_CLASS)
public class CatalogsControllerTest extends BaseControllerTest {
    @Autowired
    CatalogsRepository catalogsRepository;

    @BeforeAll
    void onBeforeAll() {
        catalogsRepository.saveAll(CatalogsControllerDataProvider.catalogs);
        System.out.println(CatalogDocument.class.getName() + " added : " + catalogsRepository.count()); 
    }
    
    @AfterEach
    void onAfterAll() {
        catalogsRepository.deleteAll();
    }

    @ParameterizedTest
    @MethodSource("com.gauravd70.ecommerce.controllers.dataproviders.CatalogsControllerDataProvider#getCatalogRequestTestCases")
    void givenGetCatalogsRequest_whenValid_thenReturnCatalogList(GetCatalogsRequestTestCase testCase) throws Exception {
        String categoryId = testCase.getCategoryId().toString();
        GetCatalogsRequest request = testCase.getRequest();

        String response = mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/v1/"+categoryId)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(request))
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        GetCatalogsResponse actualGetCatalogResponse = objectMapper.readValue(response, GetCatalogsResponse.class);

        List<CatalogDocument> catalogDocuments;
        
        if(request.getLastOffset() == null) {
            catalogDocuments = catalogsRepository.findFirst20AllByCategoryId(categoryId);
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
}
