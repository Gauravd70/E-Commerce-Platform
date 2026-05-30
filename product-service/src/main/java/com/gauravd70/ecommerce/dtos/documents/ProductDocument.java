package com.gauravd70.ecommerce.dtos.documents;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "products")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {
    private ObjectId id;
    private String brand;
    private String model;
    private double price;
    private String description;
    private long sellerId;
    @Builder.Default
    private boolean active = false;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;
    private String categoryId;
    private List<ImageInfoDocument> images;
    private Map<String, String> attributes;
}
