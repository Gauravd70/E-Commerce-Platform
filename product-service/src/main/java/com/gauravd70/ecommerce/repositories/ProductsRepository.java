package com.gauravd70.ecommerce.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.documents.ProductDocument;

@Repository
public interface ProductsRepository extends MongoRepository<ProductDocument, ObjectId>, ProductMongoTemplate {

}
