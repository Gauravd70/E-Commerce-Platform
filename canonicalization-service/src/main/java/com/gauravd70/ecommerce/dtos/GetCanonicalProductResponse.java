package com.gauravd70.ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCanonicalProductResponse {
    private String id;
    private String name;
    private String groupId;
    private String createdAt;
    private String updatedAt; 
}
