package com.gauravd70.ecommerce.controllers;

import org.bson.types.ObjectId;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
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
import org.testcontainers.utility.DockerImageName;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.ecommerce.dtos.requests.PatchCategoryRequest;
import com.gauravd70.ecommerce.dtos.requests.PostCategoryRequest;
import com.gauravd70.ecommerce.dtos.responses.CategoryInfoResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class CategoryControllerTest extends BaseControllerTest {
    @Container
    @ServiceConnection
    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:8.3.2"));

    @Test
    void givenPostCategoryRequest_whenNotAuthorized_thenReturn403Forbidden() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PostCategoryRequest.builder().build()))
                .cookie(getAccessToken("ROLE_CUSTOMER"))
        ).andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void givenPostCategoryRequest_whenInvalid_thenReturn400BadRequest() throws Exception {
        PostCategoryRequest request = PostCategoryRequest.builder().build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenPostCategoryRequest_whenValid_thenReturn200Ok() throws Exception {
        PostCategoryRequest request = PostCategoryRequest.builder().name("Health").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("id").isString());
    }

    @Test
    void givenPostCategoryRequest_whenDuplicate_thenReturn400BadRequest() throws Exception {
        PostCategoryRequest request = PostCategoryRequest.builder().name("Electronics").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenGetCategoriesRequest_whenValid_thenReturn200Ok() throws Exception {
        PostCategoryRequest request = PostCategoryRequest.builder().name("Home & Living").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

         mockMvc.perform(
            MockMvcRequestBuilders
                .get("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("categories").isArray())
        .andExpect(MockMvcResultMatchers.jsonPath("categories.length()").value(Matchers.greaterThan(0)));
    }

     @Test
    void givenGetCategoryRequest_whenInvalid_thenReturn400() throws Exception {
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/categories/v1/"+new ObjectId().toString())
                .cookie(getAccessToken("ROLE_SELLER"))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenGetCategoryRequest_whenValid_thenReturn200Ok() throws Exception {
        PostCategoryRequest request = PostCategoryRequest.builder().name("Games").build();

        String postCategoryResponseString = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

        GenericResponse response = objectMapper.readValue(postCategoryResponseString, GenericResponse.class);

        CategoryInfoResponse expectedResponse = CategoryInfoResponse.builder().id(response.getId()).name("Games").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/categories/v1/"+response.getId())
                .cookie(getAccessToken("ROLE_SELLER"))
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    void givenPatchCategoryRequest_whenValid_thenReturn200Ok() throws Exception {
        PostCategoryRequest request = PostCategoryRequest.builder().name("Mobiles").build();

        String postCategoryResponseString = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

        GenericResponse response = objectMapper.readValue(postCategoryResponseString, GenericResponse.class);

        PatchCategoryRequest patchRequest = PatchCategoryRequest.builder().name("Tablets").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .patch("/categories/v1/"+response.getId())
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void givenPatchCategoryRequest_whenDuplicate_thenReturn400BadRequest() throws Exception {
        PostCategoryRequest request1 = PostCategoryRequest.builder().name("Laptop").build();

        String postCategoryResponseString1 = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1))
        ).andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

        PostCategoryRequest request2 = PostCategoryRequest.builder().name("Food").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2))
        ).andExpect(MockMvcResultMatchers.status().isCreated());

        GenericResponse response = objectMapper.readValue(postCategoryResponseString1, GenericResponse.class);

        PatchCategoryRequest patchRequest = PatchCategoryRequest.builder().name("Food").build();

        mockMvc.perform(
            MockMvcRequestBuilders
                .patch("/categories/v1/"+response.getId())
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchRequest))
        ).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void givenDeleteCategoryRequest_whenValid_thenReturn200Ok() throws Exception {
        PostCategoryRequest request = PostCategoryRequest.builder().name("Decor").build();

        String postCategoryResponseString = mockMvc.perform(
            MockMvcRequestBuilders
                .post("/categories/v1")
                .cookie(getAccessToken("ROLE_SELLER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(MockMvcResultMatchers.status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

        GenericResponse response = objectMapper.readValue(postCategoryResponseString, GenericResponse.class);

        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/categories/v1/"+response.getId())
                .cookie(getAccessToken("ROLE_SELLER"))
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
