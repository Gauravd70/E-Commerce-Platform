package com.gauravd70.ecommerce.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.documents.ProductDocument;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PutProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.mapper.ProductMapper;
import com.gauravd70.ecommerce.repositories.ProductsRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ProductMapper productMapper;

    @Override
    public GenericResponse createProduct(PostProductRequest request, Claims claims) {
        // TODO call Catalog Service to get the canonical group

        ObjectId groupId = new ObjectId(); 

        ProductDocument productDocument = productMapper.toProductDocument(request, Long.parseLong(claims.getSubject()), groupId);

        productDocument = productsRepository.save(productDocument);

        // TODO send event to inventory service

        return GenericResponse.builder().id(productDocument.getId().toString()).message("Product added successfully").build();
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

        System.out.println(productDocument.toString());
        
        return productMapper.toGetProductResponse(productDocument);
    }

    @Override
    public GenericResponse updateProduct(String productId, PutProductRequest request) throws BadRequestException, JsonProcessingException {
        ObjectId id;
        
        try {
            id = productMapper.toObjectId(productId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        ProductDocument productDocument = productsRepository.findById(id).orElseThrow(() -> new BadRequestException());
        
        // TODO call Catalog Service to get the canonical group

        ObjectId groupId = productDocument.getGroupId();

        productDocument = productMapper.updateProductDocument(productDocument, request, groupId);

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
}
