package com.gauravd70.ecommerce.repositories;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.documents.ProductDocument;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductMongoTemplateImpl implements ProductMongoTemplate {
    private final MongoTemplate mongoTemplate;

    @Override
    public void updateProductStatus(ObjectId id, boolean status) {
        Query query = new Query(Criteria.where("_id").eq(id));
        Update update = new Update().set("active", status);

        mongoTemplate.updateFirst(query, update, ProductDocument.class);
    }

    @Override
    public Optional<ProductDocument> findAndDeleteById(ObjectId id) {
        Query query = new Query(Criteria.where("_id").eq(id));
        return Optional.of(mongoTemplate.findAndRemove(query, ProductDocument.class));
    }
}
