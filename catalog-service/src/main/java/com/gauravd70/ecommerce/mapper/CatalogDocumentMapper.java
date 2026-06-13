package com.gauravd70.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.intermediates.ExtractedProduct;

@Mapper(componentModel = "spring")
public interface CatalogDocumentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "attributes", source = "variantAttributes")
    public CatalogDocument toCatalogDocument(ExtractedProduct extractedProduct);
}
