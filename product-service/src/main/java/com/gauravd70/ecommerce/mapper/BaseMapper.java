package com.gauravd70.ecommerce.mapper;

import org.bson.types.ObjectId;

public interface BaseMapper {
    public default ObjectId toObjectId(String id) throws IllegalArgumentException{
        return new ObjectId(id);
    }

    public default String objectIdToString(ObjectId id) {
        if(id == null) {
            return null;
        }

        return id.toString();
    }
}
