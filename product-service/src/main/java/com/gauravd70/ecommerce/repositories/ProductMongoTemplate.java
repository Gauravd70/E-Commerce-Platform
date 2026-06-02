package com.gauravd70.ecommerce.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;

import com.gauravd70.ecommerce.dtos.documents.ProductDocument;

public interface ProductMongoTemplate {
    /**
     * Update the status of the product
     * 
     * @param id {@link ObjectId}
     * @param status {@link Boolean}
     */
    public void updateProductStatus(ObjectId id, boolean status);

    /**
     * Delete a product and return the product document
     * 
     * @param id {@link ObjectId}
     * @return {@link Optional}<{@link ProductDocument}>
     */
    public Optional<ProductDocument> findAndDeleteById(ObjectId id);
}
