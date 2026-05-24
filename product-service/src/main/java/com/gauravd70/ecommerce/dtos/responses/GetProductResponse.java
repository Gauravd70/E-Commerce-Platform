package com.gauravd70.ecommerce.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResponse {
    private long id;
    private String name;
    private double price;
    private String description;
    private List<ImageInfo> images;
    private List<String> categories;
}
