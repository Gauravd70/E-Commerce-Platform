package com.gauravd70.ecommerce.mapper;

import java.util.Map;

import org.mapstruct.Mapper;

import com.gauravd70.ecommerce.dtos.intermediates.NormalizedProduct;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

@Mapper(componentModel = "spring")
public interface NormalizationMapper {
    public default String normalize(String input) {
        if(input == null) {
            return null;
        }

        return input.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    public Map<String, String> normalize(Map<String, String> attributes);

    public NormalizedProduct toNormalizedProduct(ProductActionsMessage message);
}
