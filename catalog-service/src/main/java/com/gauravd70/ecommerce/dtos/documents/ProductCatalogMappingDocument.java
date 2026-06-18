package com.gauravd70.ecommerce.dtos.documents;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "product_catalog_mappings")
public class ProductCatalogMappingDocument {
    private ObjectId id;
    private String productId;
    private String familyId;
    private String variantId;
}
