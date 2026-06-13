package com.gauravd70.ecommerce.dtos.intermediates;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedProduct {
    private String name;
    private String categoryId;
    private String productId;
    private Map<String, String> familyAttributes;
    private Map<String, String> variantAttributes;
    private String familyIdRepresentation;
    private String variantIdRepresentation;
    private String familyId;
    private String variantId;
}
