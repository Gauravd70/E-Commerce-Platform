package com.gauravd70.ecommerce.dtos.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_category_mappings")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryMappingEntity {
    @EmbeddedId
    private ProductCategoryMappingId id;
}
