package com.gauravd70.ecommerce.mapper;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.gauravd70.ecommerce.dtos.intermediates.ExtractedProduct;
import com.gauravd70.ecommerce.dtos.intermediates.NormalizedProduct;
import com.gauravd70.ecommerce.dtos.messages.CategoryMessage;

@Component
public class ExtractionMapper {
    public Map<String, String> extractByVariant(Map<String, String> attributes, List<String> variantAttributes) {
        if(variantAttributes == null || attributes == null) {
            return Collections.emptyMap();
        }

        Map<String, String> resultMap = new HashMap<>();

        for(String variant : variantAttributes) {
            if(attributes.containsKey(variant)) {
                resultMap.put(variant, attributes.get(variant));
            }
        }

        return resultMap;
    }

    public ExtractedProduct extract(NormalizedProduct normalizedProduct, CategoryMessage categoryMessage) {
        if(normalizedProduct == null || categoryMessage == null || categoryMessage.getVariantAttributes() == null) {
            return null;
        }

        ExtractedProduct.ExtractedProductBuilder productBuilder = ExtractedProduct.builder();

        productBuilder.familyAttributes(null);
        productBuilder.variantAttributes(extractByVariant(normalizedProduct.getAttributes(), categoryMessage.getVariantAttributes()));

        return productBuilder.build();
    } 
}
