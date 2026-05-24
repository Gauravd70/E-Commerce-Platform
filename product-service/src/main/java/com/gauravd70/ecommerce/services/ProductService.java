package com.gauravd70.ecommerce.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PutProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;

import io.jsonwebtoken.Claims;

public interface ProductService {
    /**
     * Create a new product listing
     * 
     * @param request {@link PostProductRequest}
     * @param claims {@link Claims}
     * @return {@link GenericResponse}
     */
    public GenericResponse createProduct(PostProductRequest request, Claims claims);

    /**
     * Get the information of the product for the given product Id 
     * 
     * @param productId {@link String}
     * @return {@link GetProductResponse}
     * @throws BadRequestException
     */
    public GetProductResponse getProduct(String productId) throws BadRequestException;

    /**
     * Update the product details
     * 
     * @param productId {@link String}
     * @param request {@link PutProductRequest}
     * @return {@link GenericResponse}
     * @throws BadRequestException
     * @throws JsonProcessingException
     */
    public GenericResponse updateProduct(String productId, PutProductRequest request) throws BadRequestException, JsonProcessingException;
}
