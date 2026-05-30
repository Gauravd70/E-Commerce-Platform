package com.gauravd70.ecommerce.dtos;

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

@Document(collection = "canonical_products")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanonicalProductDocument {
    @Id
    private ObjectId id;
    private String name;
    private String canonicalRepresentation;
    private String groupId;
    private Map<String, String> attributes;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
}
