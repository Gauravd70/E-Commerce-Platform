package com.gauravd70.ecommerce.dtos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProductCategoryMappingId {
    @Column(name = "product_id")
    private long productId;
    @Column(name = "category_id")
    private long categoryId;
}
