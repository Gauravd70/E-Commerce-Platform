package com.gauravd70.ecommerce.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;

public interface CatalogsRepository extends MongoRepository<CatalogDocument, ObjectId> {

}
