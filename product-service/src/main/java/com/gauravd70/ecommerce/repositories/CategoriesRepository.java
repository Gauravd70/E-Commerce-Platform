package com.gauravd70.ecommerce.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.entities.CategoryEntity;

@Repository
public interface CategoriesRepository extends CrudRepository<CategoryEntity, Long> {

    @Query(value = """
        SELECT c.name
        FROM categories c
        JOIN product_category_mappings pcm
            ON c.id = pcm.category_id
        WHERE pcm.product_id = :productId
        """, nativeQuery = true)
    List<String> findAllCategoriesByProductId(@Param("productId") long productId);
}
