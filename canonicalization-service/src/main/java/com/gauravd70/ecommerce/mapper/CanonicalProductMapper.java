package com.gauravd70.ecommerce.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;

import com.gauravd70.ecommerce.dtos.CanonicalProductDocument;
import com.gauravd70.ecommerce.dtos.GetCanonicalProductResponse;
import com.gauravd70.ecommerce.dtos.PostCanonicalProductRequest;

@Mapper(componentModel = "spring")
public interface CanonicalProductMapper {
    public default String objectIdToString(ObjectId id) {
        if(id == null) {
            return null;
        }

        return id.toString();
    }

    public default ObjectId toObjectId(String id) throws IllegalArgumentException {
        return new ObjectId(id);
    }

    GetCanonicalProductResponse tGetCanonicalProductResponse(CanonicalProductDocument document);
}
