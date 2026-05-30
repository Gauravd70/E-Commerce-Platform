package com.gauravd70.ecommerce.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.CanonicalProductDocument;

@Repository
public interface CanonicalProductsRepository extends MongoRepository<CanonicalProductDocument, ObjectId>{
    public Optional<CanonicalProductDocument> findByGroupId(String groupId);
}
