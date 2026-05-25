package com.gauravd70.ecommerce.repositories;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.documents.CategoryDocument;

@Repository
public interface CategoriesRepository extends MongoRepository<CategoryDocument, ObjectId>{

}
