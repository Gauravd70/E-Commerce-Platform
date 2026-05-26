package com.gauravd70.ecommerce.repositories;

import org.bson.types.ObjectId;

public interface ProductMongoTemplate {
    /**
     * Update the status of the product
     * 
     * @param id {@link ObjectId}
     * @param status {@link Boolean}
     */
    public void updateProductStatus(ObjectId id, boolean status);
}
