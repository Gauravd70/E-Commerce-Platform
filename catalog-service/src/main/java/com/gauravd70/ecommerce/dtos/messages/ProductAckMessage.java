package com.gauravd70.ecommerce.dtos.messages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductAckMessage {
    private String productId;
    private String familyId;
    private String variantId;
    private long createdAt;
    private String version;
}
