package com.gauravd70.ecommerce.dtos.documents;

import java.time.Instant;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "catalogs")
public class CatalogDocument {
    @Id
    private ObjectId id;
    private String name;
    private String categoryId;
    private String productId;
    private String familyIdRepresentation;
    private String variantIdRepresentation;
    private String familyId;
    private String variantId;
    private Map<String, String> attributes;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
