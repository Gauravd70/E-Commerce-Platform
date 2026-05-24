package com.gauravd70.ecommerce.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.entities.ProductCategoryMappingEntity;
import com.gauravd70.ecommerce.dtos.entities.ProductCategoryMappingId;

@Repository
public interface ProductCategoryMappingsRepository extends CrudRepository<ProductCategoryMappingEntity, ProductCategoryMappingId> {

}
