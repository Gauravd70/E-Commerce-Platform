package com.gauravd70.ecommerce.dtos.documents;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "categories")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDocument {
    private ObjectId id;
    @Indexed(unique = true)
    private String name;
    private boolean active;
}
