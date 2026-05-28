package com.gauravd70.ecommerce.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.PatchProductStatusRequest;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PatchProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.dtos.responses.GetProductStatusResponse;
import com.gauravd70.ecommerce.services.ProductService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GenericResponse> onPostProduct(@Valid @RequestBody PostProductRequest request, @RequestAttribute("ACCESS_TOKEN") Claims claims) {
        return productService.createProduct(request, claims);
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public GetProductResponse onGetProduct(@PathVariable(value = "productId") String productId) throws BadRequestException {
        return productService.getProduct(productId);
    }

    @PatchMapping(value = "/{productId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public GenericResponse onPutProduct(@PathVariable(value = "productId") String productId, @Valid @RequestBody PatchProductRequest request) throws JsonProcessingException, BadRequestException {
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public GenericResponse onDeleteProduct(@PathVariable(value = "productId") String productId) throws BadRequestException {
        return productService.onDeleteProduct(productId);
    }

    @PatchMapping(value = "/{productId}/status", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> onPatchProductStatus(@PathVariable(value = "productId") String productId, @RequestBody PatchProductStatusRequest request) throws BadRequestException {
        return productService.onPatchProductStatus(productId, request);
    }

    @GetMapping(value = "/{productId}/status", produces = {MediaType.APPLICATION_JSON_VALUE})
    public GetProductStatusResponse onGetProductStatus(@PathVariable(value = "productId") String productId) throws BadRequestException {
        return productService.getProductStatus(productId);
    }
}
