package com.gauravd70.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.RoleEntity;

@Repository
public interface RolesRepository extends CrudRepository<RoleEntity, Long>{
    Optional<RoleEntity> findByName(String name);
}
