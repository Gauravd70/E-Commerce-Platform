package com.gauravd70.ecommerce.dtos.requests;

import java.util.Map;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutProductRequest {
    private String name;
    @Min(value = 0)
    private double price;
    @Min(value = 0)
    private int quantity;
    private String description;
    private Map<String, String> attributes;
}
