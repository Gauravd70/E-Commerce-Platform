package com.gauravd70.ecommerce.dtos;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCanonicalProductRequest {
    private String name;
    private Map<String, String> attributes;
}
