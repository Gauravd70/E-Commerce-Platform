package com.gauravd70.ecommerce.dtos.responses;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCatalogDetails {
    private String name;
    private String familyId;
    private String variantId;
    private Map<String, String> attributes;
}
