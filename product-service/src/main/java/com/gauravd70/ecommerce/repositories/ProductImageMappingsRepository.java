package com.gauravd70.ecommerce.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.entities.ProductImageMappingEntity;

@Repository
public interface ProductImageMappingsRepository extends CrudRepository<ProductImageMappingEntity, Long> {
    public Iterable<ProductImageMappingEntity> findAllByProductId(long productId);
}
