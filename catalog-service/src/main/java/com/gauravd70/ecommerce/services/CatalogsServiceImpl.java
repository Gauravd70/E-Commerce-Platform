package com.gauravd70.ecommerce.services;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;
import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;
import com.gauravd70.ecommerce.dtos.requests.GetProductIdsRequest;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogDetails;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogsResponse;
import com.gauravd70.ecommerce.dtos.responses.GetProductIdsResponse;
import com.gauravd70.ecommerce.mapper.CatalogDocumentMapper;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;
import com.gauravd70.ecommerce.repositories.ProductCatalogMappingsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogsServiceImpl implements CatalogsService {
    private final CatalogsRepository catalogsRepository;
    private final CatalogDocumentMapper catalogDocumentMapper;
    private final ProductCatalogMappingsRepository productCatalogMappingsRepository;
    
    @Override
    public GetCatalogsResponse getCatalogs(String categoryId, GetCatalogsRequest request) throws BadRequestException {
        List<CatalogDocument> catalogs;
        
        if(request.getLastOffset() == null) {
            catalogs = catalogsRepository.findFirst20AllByCategoryId(categoryId);
        } else {
            catalogs = catalogsRepository.findFirst20ByCategoryIdAndIdGreaterThan(categoryId, new ObjectId(request.getLastOffset()));
        }

        List<GetCatalogDetails> getCatalogsResponses = catalogDocumentMapper.toGetCatalogDetailsList(catalogs);

        String lastOffset = null;

        if(catalogs.size() > 0) {
            lastOffset = catalogs.get(catalogs.size() - 1).getId().toString();
        }

        return GetCatalogsResponse.builder().catalogs(getCatalogsResponses).lastOffset(lastOffset).build();
    }

    @Override
    public GetProductIdsResponse getProductIds(String familyId, GetProductIdsRequest request) {
        List<ProductCatalogMappingDocument> productCatalogMappingDocuments = productCatalogMappingsRepository.findAllByFamilyIdAndVariantId(familyId, familyId);

        List<String> productIds = productCatalogMappingDocuments.stream().map(document -> document.getProductId()).toList();

        return GetProductIdsResponse.builder().products(productIds).build();
    }
}
