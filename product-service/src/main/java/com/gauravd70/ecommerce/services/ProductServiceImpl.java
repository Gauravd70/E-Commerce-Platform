package com.gauravd70.ecommerce.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.entities.ProductEntity;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PutProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.dtos.responses.ImageInfo;
import com.gauravd70.ecommerce.mapper.ProductMapper;
import com.gauravd70.ecommerce.repositories.CategoriesRepository;
import com.gauravd70.ecommerce.repositories.ProductImageMappingsRepository;
import com.gauravd70.ecommerce.repositories.ProductsRepository;

import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductsRepository productsRepository;
    private final ProductImageMappingsRepository productImageMappingsRepository;
    private final CategoriesRepository categoriesRepository;
    private final ProductMapper productMapper;

    @Override
    public GenericResponse createProduct(PostProductRequest request, Claims claims) {
        // TODO call Catalog Service to get the canonical group
        long groupId = System.currentTimeMillis();

        ProductEntity productEntity = productMapper.toProductEntity(request, Long.parseLong(claims.getSubject()), groupId);

        productEntity = productsRepository.save(productEntity);

        // TODO send event to inventory service

        return GenericResponse.builder().id(String.valueOf(productEntity.getId())).message("Product added successfully").build();
    }

    @Override
    public GetProductResponse getProduct(String productId) throws BadRequestException {
        long productIdLong;
        
        try {
            productIdLong = Long.parseLong(productId);
        } catch(NumberFormatException e) {
            throw new BadRequestException();
        }

        ProductEntity productEntity = productsRepository.findById(productIdLong).orElseThrow(() -> new BadRequestException());

        List<ImageInfo> images = productMapper.toImageInfoList(productImageMappingsRepository.findAllByProductId(productIdLong));

        List<String> categories = categoriesRepository.findAllCategoriesByProductId(productIdLong);
        
        return productMapper.toGetProductResponse(productEntity, images, categories);
    }

    @Override
    @Transactional
    public GenericResponse updateProduct(String productId, PutProductRequest request) throws BadRequestException, JsonProcessingException {
        long productIdLong;
        
        try {
            productIdLong = Long.parseLong(productId);
        } catch(NumberFormatException e) {
            throw new BadRequestException();
        }

        ProductEntity productEntity = productsRepository.findById(productIdLong).orElseThrow(() -> new BadRequestException());
        
        // TODO call Catalog Service to get the canonical group
        long groupId = productEntity.getGroupId();

        if(!Objects.equals(request.getName(), productEntity.getName())) {
            productEntity.setName(request.getName());
        }

        if(!Objects.equals(request.getPrice(), productEntity.getPrice())) {
            productEntity.setPrice(request.getPrice());
        }

        if(!Objects.equals(request.getDescription(), productEntity.getDescription())) {
            productEntity.setDescription(request.getDescription());
        }

        String attributeString = productMapper.mapToJsonString(request.getAttributes());

        if(!Objects.equals(attributeString, productEntity.getAttributes())) {
            productEntity.setAttributes(attributeString);
        }

        if(Objects.equals(groupId, productEntity.getGroupId())) {
            productEntity.setGroupId(groupId);
        }

        // TODO send event to Inventory Service to update the inventory

        return GenericResponse.builder().message("Product updated successfully").build();
    }
}
