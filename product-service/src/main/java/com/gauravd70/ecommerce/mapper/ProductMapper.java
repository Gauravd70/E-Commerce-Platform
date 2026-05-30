package com.gauravd70.ecommerce.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.gauravd70.ecommerce.dtos.documents.ImageInfoDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductDocument;
import com.gauravd70.ecommerce.dtos.requests.ImageInfoRequest;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;
import com.gauravd70.ecommerce.dtos.requests.PatchProductRequest;
import com.gauravd70.ecommerce.dtos.responses.GetProductResponse;
import com.gauravd70.ecommerce.dtos.responses.ImageInfoResponse;

@Mapper(componentModel = "spring")
public interface ProductMapper extends BaseMapper{

    @Mapping(target = "id", ignore = true)
    public ImageInfoDocument toImageInfoDocument(ImageInfoRequest request);

    public ImageInfoResponse toImageInfoResponse(ImageInfoDocument document);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public ProductDocument toProductDocument(PostProductRequest request, long sellerId);

    public GetProductResponse toGetProductResponse(ProductDocument document);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "sellerId", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public ProductDocument updateProductDocument(@MappingTarget ProductDocument original, PatchProductRequest request);
}
