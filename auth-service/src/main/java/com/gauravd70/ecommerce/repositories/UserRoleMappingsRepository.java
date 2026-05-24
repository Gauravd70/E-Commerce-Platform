package com.gauravd70.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.UserRoleMappingEntity;
import com.gauravd70.ecommerce.dtos.UserRoleMappingId;

@Repository
public interface UserRoleMappingsRepository extends CrudRepository<UserRoleMappingEntity, UserRoleMappingId>{
    public Optional<UserRoleMappingEntity> findByIdUserId(long userId);
}
