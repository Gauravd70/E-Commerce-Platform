package com.gauravd70.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;

@Mapper(componentModel = "spring")
public interface ProductCatalogMappingDocumentMapper {
    @Mapping(target = "id", ignore = true)
    public ProductCatalogMappingDocument toProductCatalogMappingDocument(CatalogDocument catalogDocument, String productId);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    public ProductCatalogMappingDocument toProductCatalogMappingDocument(@MappingTarget ProductCatalogMappingDocument target, CatalogDocument catalogDocument);
}
