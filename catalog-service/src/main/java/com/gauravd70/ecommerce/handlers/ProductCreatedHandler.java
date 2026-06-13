package com.gauravd70.ecommerce.handlers;

import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.dtos.intermediates.ExtractedProduct;
import com.gauravd70.ecommerce.dtos.intermediates.NormalizedProduct;
import com.gauravd70.ecommerce.dtos.messages.ProductAction;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;
import com.gauravd70.ecommerce.mapper.ExtractionMapper;
import com.gauravd70.ecommerce.mapper.NormalizationMapper;
import com.gauravd70.ecommerce.repositories.CatalogsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProductCreatedHandler extends ProductHandler {
    private final CatalogsRepository catalogsRepository;
    private final NormalizationMapper normalizationMapper;
    private final ExtractionMapper extractionMapper;

    @Override
    protected String getAction() {
        return ProductAction.CREATE.name();
    }

    @Async("productActionsExecutor")
    @Override
    public void onHandleMessage(ProductActionsMessage message) {
        super.onHandleMessage(message);

        NormalizedProduct normalizedProduct = normalizationMapper.toNormalizedProduct(message);

        ExtractedProduct extractedProduct = extractionMapper.extract(normalizedProduct, message.getCategory());

        
    }
}
