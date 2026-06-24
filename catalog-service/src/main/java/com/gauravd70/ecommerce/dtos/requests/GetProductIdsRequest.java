package com.gauravd70.ecommerce.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetProductIdsRequest {
    private String variantId;
    private String lastOffset;
}
