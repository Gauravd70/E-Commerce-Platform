package com.gauravd70.ecommerce.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.UserEntity;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{
    public Optional<UserEntity> findByUsername(String username);
}
