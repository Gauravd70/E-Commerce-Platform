package com.gauravd70.ecommerce.dtos.documents;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfoDocument {
    @Builder.Default
    private ObjectId id = new ObjectId();
    private String url;
    private String type;
    private int displayOrder;
}
