package com.gauravd70.ecommerce.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogDetails;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogsResponse;
import com.gauravd70.ecommerce.mapper.CatalogDocumentMapper;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogsServiceImpl implements CatalogsService {
    private final CatalogsRepository catalogsRepository;
    private final CatalogDocumentMapper catalogDocumentMapper;
    
    @Override
    public GetCatalogsResponse getCatalogs(String categoryId, GetCatalogsRequest request) throws BadRequestException {
        List<CatalogDocument> catalogs = catalogsRepository.findAllByCategoryIdGreaterThanProductId(categoryId, request.getLastOffset());

        List<GetCatalogDetails> getCatalogsResponses = catalogDocumentMapper.toGetCatalogDetailsList(catalogs);

        return GetCatalogsResponse.builder().catalogs(getCatalogsResponses).build();
    }
}
