package com.gauravd70.ecommerce.dtos.documents;

import java.time.Instant;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
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
@CompoundIndex(
    name = "familyId_variantId_idx",
    def = "{'familyId': 1, 'variantId': 1}",
    unique = true
)
public class CatalogDocument {
    @Id
    private ObjectId id;
    private String familyId;
    private String variantId;
    private String name;
    private String categoryId;
    private String familyIdRepresentation;
    private String variantIdRepresentation;
    private Map<String, String> attributes;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
