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
public class NormalizedProduct {
    private String brand;
    private String model;
    private Map<String, String> attributes;
}
