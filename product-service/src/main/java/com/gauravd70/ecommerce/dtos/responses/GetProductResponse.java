package com.gauravd70.ecommerce.dtos.responses;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {
    private String id;
    private String brand;
    private String model;
    private double price;
    private String description;
    private long sellerId;
    private List<ImageInfoResponse> images;
    private String categoryId;
    private Map<String, String> attributes;
    private Instant createdAt;
    private Instant updatedAt;
}
