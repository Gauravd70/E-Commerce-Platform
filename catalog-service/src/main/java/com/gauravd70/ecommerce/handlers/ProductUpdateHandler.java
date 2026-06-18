package com.gauravd70.ecommerce.handlers;

import java.util.Optional;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;
import com.gauravd70.ecommerce.dtos.intermediates.ExtractedProduct;
import com.gauravd70.ecommerce.dtos.intermediates.NormalizedProduct;
import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.mapper.CatalogDocumentMapper;
import com.gauravd70.ecommerce.mapper.ExtractionMapper;
import com.gauravd70.ecommerce.mapper.NormalizationMapper;
import com.gauravd70.ecommerce.mapper.ProductCatalogMappingDocumentMapper;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;
import com.gauravd70.ecommerce.repositories.ProductCatalogMappingsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductUpdateHandler extends ProductHandler {
    private final CatalogsRepository catalogsRepository;
    private final NormalizationMapper normalizationMapper;
    private final ExtractionMapper extractionMapper;
    private final CatalogDocumentMapper catalogDocumentMapper;
    private final ProductCatalogMappingDocumentMapper productCatalogMappingDocumentMapper;
    private final ProductCatalogMappingsRepository productCatalogMappingsRepository;

    @Override
    protected String getAction() {
        return ProductAction.UPDATE.name();
    }

    @Async("productActionsExecutor")
    @Override
    public void onHandleMessage(ProductActionsMessage message) {
        super.onHandleMessage(message);
        
        NormalizedProduct normalizedProduct = normalizationMapper.toNormalizedProduct(message);

        ExtractedProduct extractedProduct = extractionMapper.extract(normalizedProduct, message);

        Optional<CatalogDocument> catalogDocumentOptional = catalogsRepository.findOneByFamilyIdAndVariantId(extractedProduct.getFamilyId(), extractedProduct.getVariantId());
        
        CatalogDocument catalogDocument;

        if(catalogDocumentOptional.isEmpty()) {
            catalogDocument = catalogDocumentMapper.toCatalogDocument(extractedProduct);

            catalogsRepository.save(catalogDocument);
        } else {
            catalogDocument = catalogDocumentOptional.get();
        }

        Optional<ProductCatalogMappingDocument> productCatalogMappingDocumentOptional = productCatalogMappingsRepository.findOneByProductId(message.getId());
        
        ProductCatalogMappingDocument productCatalogMappingDocument;

        if(productCatalogMappingDocumentOptional.isEmpty()) {
            productCatalogMappingDocument = productCatalogMappingDocumentMapper.toProductCatalogMappingDocument(catalogDocument, message.getId());
            
        } else {
            productCatalogMappingDocument = productCatalogMappingDocumentOptional.get();

            productCatalogMappingDocumentMapper.toProductCatalogMappingDocument(productCatalogMappingDocument, catalogDocument);
        }

        productCatalogMappingsRepository.save(productCatalogMappingDocument);
    }
}
