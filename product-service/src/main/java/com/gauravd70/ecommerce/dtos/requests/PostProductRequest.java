package com.gauravd70.ecommerce.dtos.requests;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostProductRequest {
    @NotBlank
    private String name;
    @PositiveOrZero
    private double price;
    @PositiveOrZero
    private int quantity;
    @NotEmpty
    private String description;
    private List<String> categories;
    @Valid
    private List<ImageInfoRequest> images;
    private Map<String, String> attributes;
}
