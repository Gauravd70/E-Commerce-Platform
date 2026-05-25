package com.gauravd70.ecommerce.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PutProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
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
    public GenericResponse onPostProduct(@Valid @RequestBody PostProductRequest request, @RequestAttribute("ACCESS_TOKEN") Claims claims) {
        return productService.createProduct(request, claims);
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public GetProductResponse onGetProduct(@PathVariable(value = "productId") String productId) throws BadRequestException {
        return productService.getProduct(productId);
    }

    @PutMapping(value = "/{productId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public GenericResponse onPutProduct(@PathVariable(value = "productId") String productId, @Valid @RequestBody PutProductRequest request) throws JsonProcessingException, BadRequestException {
        return productService.updateProduct(productId, request);
    }

    @DeleteMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public GenericResponse onDeleteProduct(@PathVariable(value = "productId") String productId) throws BadRequestException {
        return productService.onDeleteProduct(productId);
    }
}
