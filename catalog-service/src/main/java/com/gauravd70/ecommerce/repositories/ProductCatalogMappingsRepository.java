package com.gauravd70.ecommerce.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;

@Repository
public interface ProductCatalogMappingsRepository extends MongoRepository<ProductCatalogMappingDocument, ObjectId>{
    public Optional<ProductCatalogMappingDocument> findOneByProductId(String productId);

    public void deleteOneByProductId(String productId);
}
