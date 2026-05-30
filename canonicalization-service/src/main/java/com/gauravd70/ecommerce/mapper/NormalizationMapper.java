package com.gauravd70.ecommerce.mapper;

import java.util.Map;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NormalizationMapper {
    public default String normalize(String input) {
        if(input == null) {
            return null;
        }

        return input.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    public Map<String, String> normalize(Map<String, String> attributes);
}
