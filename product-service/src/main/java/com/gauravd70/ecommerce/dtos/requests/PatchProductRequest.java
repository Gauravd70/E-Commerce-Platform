package com.gauravd70.ecommerce.dtos.requests;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchProductRequest {
    private String brand;
    private String model;
    @Min(value = 0)
    private Double price;
    @Min(value = 0)
    private Integer quantity;
    private String description;
    @Valid
    private List<ImageInfoRequest> images;
    private String categoryId;
    private Map<String, String> attributes;
}
