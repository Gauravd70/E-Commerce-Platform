package com.gauravd70.ecommerce.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.ecommerce.dtos.requests.ImageInfoRequest;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PatchProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PatchProductStatusRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.dtos.responses.ImageInfoResponse;
import com.gauravd70.ecommerce.repositories.ProductsRepository;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.Cookie;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Testcontainers
public class ProductControllerTest extends BaseControllerTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:8.3.2"));

    @Autowired
    ProductsRepository productsRepository;

    void givenPostProductRequest_whenNotAuthorized_thenReturn401Unauthorized() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PostProductRequest.builder().build()))
                .cookie(getAccessToken("ROLE_CUSTOMER"))
        ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @ParameterizedTest
    @MethodSource("com.gauravd70.ecommerce.dataproviders.ProductControllerDataProvider#getInvalidPostProductRequests")
    void givenPostProductRequest_whenInvalid_thenReturn400BadRequest(PostProductRequest request) throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(getAccessToken("ROLE_SELLER"))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenPostProductRequest_whenValid_thenReturn200OK() throws Exception {
        PostProductRequest request = PostProductRequest
            .builder()
            .name("Apple MacBook Pro")
            .price(150000)
            .quantity(10)
            .description("M5 16 inch apple laptop")
            .images(List.of(
                ImageInfoRequest
                    .builder()
                    .url("https://www.image.com")
                    .type("thumbnail")
                    .displayOrder(0)
                    .build(),
                ImageInfoRequest
                    .builder()
                    .url("https://www.image2.com")
                    .type("gallery")
                    .displayOrder(1)
                    .build()))
            .categories(List.of(new ObjectId().toString(), new ObjectId().toString()))
            .build();

            String response = mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .cookie(getAccessToken("ROLE_SELLER"))
            ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString();

            GenericResponse genericResponse = objectMapper.readValue(response, GenericResponse.class);

            assertNotNull(productsRepository.findById(new ObjectId(genericResponse.getId())));
    }

    @Test
    void givenGetProductRequest_whenInvalid_thenReturn400BadRequest() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                    .get("/v1/"+(new ObjectId().toString()))
            ).andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void givenGetProductRequest_whenValid_thenReturn200OK() throws Exception {
        Cookie accessTokenCookie = getAccessToken("ROLE_SELLER");

        Claims claims = jwtUtils.getClaims(accessTokenCookie.getValue());

        Map<String, String> attributes = new HashMap<>();

        attributes.put("RAM", "8GB");
        attributes.put("OS", "MacOS");
        attributes.put("Color", "Grey");
        attributes.put("Storage", "512GB");

        List<String> categories = List.of(new ObjectId().toString(), new ObjectId().toString());

        PostProductRequest request = PostProductRequest
            .builder()
            .name("Apple MacBook Pro")
            .price(150000)
            .quantity(10)
            .description("M5 16 inch apple laptop")
            .images(List.of(
                ImageInfoRequest
                    .builder()
                    .url("https://www.image.com")
                    .type("thumbnail")
                    .displayOrder(0)
                    .build(),
                ImageInfoRequest
                    .builder()
                    .url("https://www.image2.com")
                    .type("gallery")
                    .displayOrder(1)
                    .build()))
            .categories(categories)
            .attributes(attributes)
            .build();

            String response = mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .cookie(accessTokenCookie)
            ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString();

            GenericResponse genericResponse = objectMapper.readValue(response, GenericResponse.class);

            GetProductResponse expectedResponse = GetProductResponse.builder()
                .id(genericResponse.getId())
                .name("Apple MacBook Pro")
                .price(150000)
                .description("M5 16 inch apple laptop")
                .sellerId(Long.valueOf(claims.getSubject()))
                .images(List.of(
                    ImageInfoResponse
                        .builder()
                        .url("https://www.image.com")
                        .type("thumbnail")
                        .displayOrder(0)
                        .build(),
                    ImageInfoResponse
                        .builder()
                        .url("https://www.image2.com")
                        .type("gallery")
                        .displayOrder(1)
                        .build()))
                .categories(categories)
                .attributes(attributes)
                .build();

            String actualResponseString = mockMvc.perform(MockMvcRequestBuilders.get("/v1/"+genericResponse.getId()).cookie(accessTokenCookie))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

            GetProductResponse actualResponse = objectMapper.readValue(actualResponseString, GetProductResponse.class);

            Assertions.assertThat(actualResponse)
                    .usingRecursiveComparison()
                    .ignoringFields("images.id", "sellerId", "groupId", "createdAt", "updatedAt")
                    .isEqualTo(expectedResponse);
            
            Assertions.assertThat(actualResponse.getSellerId()).isNotNull();
            Assertions.assertThat(actualResponse.getGroupId()).isNotNull();
            Assertions.assertThat(actualResponse.getCreatedAt()).isNotNull();
            Assertions.assertThat(actualResponse.getUpdatedAt()).isNotNull();

            Assertions.assertThat(actualResponse.getImages())
                    .extracting(ImageInfoResponse::getId)
                    .doesNotContainNull();   
    }

    @Test
    void givenPatchProductRequest_whenInvalid_thenReturn400BadRequest() throws Exception{
        Cookie accessTokenCookie = getAccessToken("ROLE_SELLER");

        Map<String, String> attributes = new HashMap<>();

        attributes.put("RAM", "8GB");
        attributes.put("OS", "MacOS");
        attributes.put("Color", "Grey");
        attributes.put("Storage", "512GB");

        List<String> categories = List.of(new ObjectId().toString(), new ObjectId().toString());

        PostProductRequest request = PostProductRequest
            .builder()
            .name("Apple MacBook Pro")
            .price(150000)
            .quantity(10)
            .description("M5 16 inch apple laptop")
            .images(List.of(
                ImageInfoRequest
                    .builder()
                    .url("https://www.image.com")
                    .type("thumbnail")
                    .displayOrder(0)
                    .build(),
                ImageInfoRequest
                    .builder()
                    .url("https://www.image2.com")
                    .type("gallery")
                    .displayOrder(1)
                    .build()))
            .categories(categories)
            .attributes(attributes)
            .build();

            String response = mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
                    .cookie(accessTokenCookie)
            ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString();

            GenericResponse genericResponse = objectMapper.readValue(response, GenericResponse.class);

            PatchProductRequest patchProductRequest = PatchProductRequest.builder().price(-1d).build(); 

            mockMvc.perform(
                MockMvcRequestBuilders
                    .patch("/v1/"+genericResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(accessTokenCookie)
                    .content(objectMapper.writeValueAsString(patchProductRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenPatchProductRequest_whenValid_thenReturn200Ok() throws Exception{
        Cookie accessTokenCookie = getAccessToken("ROLE_SELLER");

        Claims claims = jwtUtils.getClaims(accessTokenCookie.getValue());

        Map<String, String> attributes = new HashMap<>();

        attributes.put("RAM", "8GB");
        attributes.put("OS", "MacOS");
        attributes.put("Color", "Grey");
        attributes.put("Storage", "512GB");

        List<String> categories = List.of(new ObjectId().toString(), new ObjectId().toString());

        PostProductRequest postProductRequest = PostProductRequest
            .builder()
            .name("Apple MacBook Pro")
            .price(150000)
            .quantity(10)
            .description("M5 16 inch apple laptop")
            .images(List.of(
                ImageInfoRequest
                    .builder()
                    .url("https://www.image.com")
                    .type("thumbnail")
                    .displayOrder(0)
                    .build(),
                ImageInfoRequest
                    .builder()
                    .url("https://www.image2.com")
                    .type("gallery")
                    .displayOrder(1)
                    .build()))
            .categories(categories)
            .attributes(attributes)
            .build();

            String postProductResponseString = mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postProductRequest))
                    .cookie(accessTokenCookie)
            ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString();

            GenericResponse genericResponse = objectMapper.readValue(postProductResponseString, GenericResponse.class);

            PatchProductRequest patchProductRequest = 
                PatchProductRequest.builder().price(100000d).description("M5 16 inch").build(); 

            GenericResponse expectedPutProductResponse = GenericResponse.builder().message("Product updated successfully").build();

            mockMvc.perform(
                MockMvcRequestBuilders
                    .patch("/v1/"+genericResponse.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .cookie(accessTokenCookie)
                    .content(objectMapper.writeValueAsString(patchProductRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedPutProductResponse)));

            GetProductResponse expectedGetProductResponse = GetProductResponse.builder()
                .id(genericResponse.getId())
                .name("Apple MacBook Pro")
                .price(100000)
                .description("M5 16 inch")
                .sellerId(Long.valueOf(claims.getSubject()))
                .images(List.of(
                    ImageInfoResponse
                        .builder()
                        .url("https://www.image.com")
                        .type("thumbnail")
                        .displayOrder(0)
                        .build(),
                    ImageInfoResponse
                        .builder()
                        .url("https://www.image2.com")
                        .type("gallery")
                        .displayOrder(1)
                        .build()))
                .categories(categories)
                .attributes(attributes)
                .build();

            String actualGetProductResponseString = mockMvc.perform(MockMvcRequestBuilders.get("/v1/"+genericResponse.getId()).cookie(accessTokenCookie))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

            GetProductResponse actualResponse = objectMapper.readValue(actualGetProductResponseString, GetProductResponse.class);

            Assertions.assertThat(actualResponse)
                    .usingRecursiveComparison()
                    .ignoringFields("images.id", "sellerId", "groupId", "createdAt", "updatedAt")
                    .isEqualTo(expectedGetProductResponse);
            
            Assertions.assertThat(actualResponse.getSellerId()).isNotNull();
            Assertions.assertThat(actualResponse.getGroupId()).isNotNull();
            Assertions.assertThat(actualResponse.getCreatedAt()).isNotNull();
            Assertions.assertThat(actualResponse.getUpdatedAt()).isNotNull();

            Assertions.assertThat(actualResponse.getImages())
                    .extracting(ImageInfoResponse::getId)
                    .doesNotContainNull();
    }

    @Test
    void givenDeleteProductRequest_whenValid_thenReturn200O() throws Exception {
        Cookie accessTokenCookie = getAccessToken("ROLE_SELLER");

        Map<String, String> attributes = new HashMap<>();

        attributes.put("RAM", "8GB");
        attributes.put("OS", "MacOS");
        attributes.put("Color", "Grey");
        attributes.put("Storage", "512GB");

        List<String> categories = List.of(new ObjectId().toString(), new ObjectId().toString());

        PostProductRequest postProductRequest = PostProductRequest
            .builder()
            .name("Apple MacBook Pro")
            .price(150000)
            .quantity(10)
            .description("M5 16 inch apple laptop")
            .images(List.of(
                ImageInfoRequest
                    .builder()
                    .url("https://www.image.com")
                    .type("thumbnail")
                    .displayOrder(0)
                    .build(),
                ImageInfoRequest
                    .builder()
                    .url("https://www.image2.com")
                    .type("gallery")
                    .displayOrder(1)
                    .build()))
            .categories(categories)
            .attributes(attributes)
            .build();

            String postProductResponseString = mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postProductRequest))
                    .cookie(accessTokenCookie)
            ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString();

            GenericResponse genericResponse = objectMapper.readValue(postProductResponseString, GenericResponse.class);

            mockMvc.perform(MockMvcRequestBuilders.delete("/v1/"+genericResponse.getId()).cookie(accessTokenCookie))
                .andExpect(MockMvcResultMatchers.status().isOk());

            mockMvc.perform(MockMvcRequestBuilders.get("/v1/"+genericResponse.getId()).cookie(accessTokenCookie))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenPatchStatusRequest_whenValid_thenReturn200Ok() throws Exception {
         Cookie accessTokenCookie = getAccessToken("ROLE_SELLER");

        Map<String, String> attributes = new HashMap<>();

        attributes.put("RAM", "8GB");
        attributes.put("OS", "MacOS");
        attributes.put("Color", "Grey");
        attributes.put("Storage", "512GB");

        List<String> categories = List.of(new ObjectId().toString(), new ObjectId().toString());

        PostProductRequest postProductRequest = PostProductRequest
            .builder()
            .name("Apple MacBook Pro")
            .price(150000)
            .quantity(10)
            .description("M5 16 inch apple laptop")
            .images(List.of(
                ImageInfoRequest
                    .builder()
                    .url("https://www.image.com")
                    .type("thumbnail")
                    .displayOrder(0)
                    .build(),
                ImageInfoRequest
                    .builder()
                    .url("https://www.image2.com")
                    .type("gallery")
                    .displayOrder(1)
                    .build()))
            .categories(categories)
            .attributes(attributes)
            .build();

            String postProductResponseString = mockMvc.perform(
                MockMvcRequestBuilders
                    .post("/v1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(postProductRequest))
                    .cookie(accessTokenCookie)
            ).andExpect(MockMvcResultMatchers.status().isCreated())
            .andReturn().getResponse().getContentAsString();

            GenericResponse genericResponse = objectMapper.readValue(postProductResponseString, GenericResponse.class);

            mockMvc.perform(MockMvcRequestBuilders.get("/v1/"+genericResponse.getId()+"/status").cookie(accessTokenCookie))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("active").value(false));

            PatchProductStatusRequest patchProductStatusRequest = PatchProductStatusRequest.builder().active(true).build();

            mockMvc.perform(
                MockMvcRequestBuilders
                    .patch("/v1/"+genericResponse.getId()+"/status")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(patchProductStatusRequest))
                    .cookie(accessTokenCookie)
            ).andExpect(MockMvcResultMatchers.status().isOk());
                
            mockMvc.perform(MockMvcRequestBuilders.get("/v1/"+genericResponse.getId()+"/status").cookie(accessTokenCookie))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("active").value(true));
    }
}
