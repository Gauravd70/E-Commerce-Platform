package com.gauravd70.ecommerce.services;

import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.configurations.RabbitMQConfigurations;
import com.gauravd70.ecommerce.dtos.documents.ProductDocument;
import com.gauravd70.ecommerce.dtos.messages.ProductCreatedMessage;
import com.gauravd70.ecommerce.dtos.requests.PatchProductStatusRequest;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PatchProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.dtos.responses.GetProductStatusResponse;
import com.gauravd70.ecommerce.mapper.ProductMapper;
import com.gauravd70.ecommerce.repositories.CategoriesRepository;
import com.gauravd70.ecommerce.repositories.ProductsRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ProductMapper productMapper;
    private final CategoriesRepository categoriesRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public ResponseEntity<GenericResponse> createProduct(PostProductRequest request, Claims claims) {
        final ProductDocument productDocument = productMapper.toProductDocument(request, Long.parseLong(claims.getSubject()));

        productsRepository.save(productDocument);
        
        categoriesRepository.findById(new ObjectId(productDocument.getCategoryId())).ifPresent(category -> {
            ProductCreatedMessage productCreatedMessage = productMapper.toProductCreatedMessage(productDocument, category);

            rabbitTemplate.convertAndSend(RabbitMQConfigurations.PRODUCT_EXCHANGE, RabbitMQConfigurations.PRODUCT_CREATED_ROUTING_KEY, productCreatedMessage);
        });

        // TODO send event to inventory service

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(GenericResponse.builder().id(productDocument.getId().toString()).message("Product added successfully").build());
    }

    @Override
    public GetProductResponse getProduct(String productId) throws BadRequestException {
        ObjectId id;
        
        try {
            id = productMapper.toObjectId(productId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        ProductDocument productDocument = productsRepository.findById(id).orElseThrow(() -> new BadRequestException());
        
        return productMapper.toGetProductResponse(productDocument);
    }

    @Override
    public GenericResponse updateProduct(String productId, PatchProductRequest request) throws BadRequestException, JsonProcessingException {
        ObjectId id;
        
        try {
            id = productMapper.toObjectId(productId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        ProductDocument productDocument = productsRepository.findById(id).orElseThrow(() -> new BadRequestException());
        
        // TODO call Catalog Service to get the canonical group

        productDocument = productMapper.updateProductDocument(productDocument, request);

        productsRepository.save(productDocument);

        // TODO send event to Inventory Service to update the inventory

        return GenericResponse.builder().message("Product updated successfully").build();
    }

    @Override
    public GenericResponse onDeleteProduct(String productId) throws BadRequestException {
        ObjectId id;
        
        try {
            id = productMapper.toObjectId(productId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        productsRepository.deleteById(id);

        // TODO send event to Inventory service to remove the inventory for the given product

        return GenericResponse.builder().message("Product deleted successfully").build();
    }

    @Override
    public ResponseEntity<Void> onPatchProductStatus(String productId, PatchProductStatusRequest request) throws BadRequestException {
        ObjectId id;
        
        try {
            id = productMapper.toObjectId(productId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        productsRepository.updateProductStatus(id, request.isActive());

        return ResponseEntity.ok().build();
    }

    @Override
    public GetProductStatusResponse getProductStatus(String productId) throws BadRequestException {
        ObjectId id;
        
        try {
            id = productMapper.toObjectId(productId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        ProductDocument productDocument = productsRepository.findById(id).orElseThrow(() -> new BadRequestException());

        return GetProductStatusResponse.builder().active(productDocument.isActive()).build();
    }
}
