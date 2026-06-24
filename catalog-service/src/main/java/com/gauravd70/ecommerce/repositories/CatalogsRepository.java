package com.gauravd70.ecommerce.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;

public interface CatalogsRepository extends MongoRepository<CatalogDocument, ObjectId> {
    public Optional<CatalogDocument> findOneByFamilyIdAndVariantId(String familyId, String variantId);

    public List<CatalogDocument> findFirst20ByCategoryIdAndIdGreaterThan(String categoryId, ObjectId id);

    public List<CatalogDocument> findFirst20ByCategoryId(String categoryId);
}
