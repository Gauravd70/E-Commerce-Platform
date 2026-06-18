package com.gauravd70.ecommerce.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.intermediates.ExtractedProduct;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogDetails;

@Mapper(componentModel = "spring")
public interface CatalogDocumentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "attributes", source = "variantAttributes")
    public CatalogDocument toCatalogDocument(ExtractedProduct extractedProduct);

    public List<GetCatalogDetails> toGetCatalogDetailsList(List<CatalogDocument> catalogs);

    public GetCatalogDetails toGetCatalogDetails(CatalogDocument catalogDocument);
}
