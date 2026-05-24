package com.gauravd70.ecommerce.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.entities.ProductEntity;

@Repository
public interface ProductsRepository extends CrudRepository<ProductEntity, Long> {

}
