package com.gauravd70.ecommerce.dtos.documents;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "productCatalogMappings")
public class ProductCatalogMappingDocument {
    @Id
    private ObjectId id;
    private String productId;
    private String familyId;
    private String variantId;
}
