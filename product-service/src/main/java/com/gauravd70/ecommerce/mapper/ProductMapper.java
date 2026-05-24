package com.gauravd70.ecommerce.mapper;

import java.util.List;
import java.util.Map;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gauravd70.ecommerce.dtos.entities.ProductEntity;
import com.gauravd70.ecommerce.dtos.entities.ProductImageMappingEntity;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.dtos.responses.ImageInfo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class ProductMapper {
    private final ObjectMapper objectMapper;

    public String mapToJsonString(Map<String, String> map) throws JsonProcessingException {
        if(map == null) {
            return "{}";
        }

        return objectMapper.writeValueAsString(map);
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public abstract ProductEntity toProductEntity(PostProductRequest request, long sellerId, long groupId);

    public abstract GetProductResponse toGetProductResponse(ProductEntity entity, List<ImageInfo> images, List<String> categories);

    public abstract ImageInfo toImageInfo(ProductImageMappingEntity entity);

    public abstract List<ImageInfo> toImageInfoList(Iterable<ProductImageMappingEntity> iterable);
}
