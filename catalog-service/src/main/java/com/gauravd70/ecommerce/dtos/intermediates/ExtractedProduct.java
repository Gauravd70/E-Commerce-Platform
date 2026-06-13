package com.gauravd70.ecommerce.dtos.intermediates;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtractedProduct {
    private Map<String, String> familyAttributes;
    private Map<String, String> variantAttributes;
}
