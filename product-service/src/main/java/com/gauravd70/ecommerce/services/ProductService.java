package com.gauravd70.ecommerce.services;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.PatchProductStatusRequest;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PatchProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.dtos.responses.GetProductStatusResponse;

import io.jsonwebtoken.Claims;

public interface ProductService {
    /**
     * Create a new product listing
     * 
     * @param request {@link PostProductRequest}
     * @param claims {@link Claims}
     * @return {@link ResponseEntity}<{@link GenericResponse}>
     */
    public ResponseEntity<GenericResponse> createProduct(PostProductRequest request, Claims claims);

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
     * @param request {@link PatchProductRequest}
     * @return {@link GenericResponse}
     * @throws BadRequestException
     * @throws JsonProcessingException
     */
    public GenericResponse updateProduct(String productId, PatchProductRequest request) throws BadRequestException, JsonProcessingException;

    /**
     * Delete a product
     * 
     * @param productId {@link String}
     * @return {@link ResponseEntity}<{@link Void}>
     * @throws BadRequestException
     */
    public GenericResponse onDeleteProduct(String productId) throws BadRequestException;

    /**
     * Update status of a product
     * 
     * @param productId {@link String}
     * @param request {@link PatchProductStatusRequest}
     * @return {@link ResponseEntity}<{@link Void}>
     * @throws BadRequestException
     */
    public ResponseEntity<Void> onPatchProductStatus(String productId, PatchProductStatusRequest request) throws BadRequestException;

    /**
     * Get the status of the product
     * 
     * @param productId {@link String}
     * @return {@link GetProductStatusResponse}
     * @throws BadRequestException
     */
    public GetProductStatusResponse getProductStatus(String productId) throws BadRequestException;
}
