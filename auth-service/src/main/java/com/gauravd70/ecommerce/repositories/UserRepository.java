package com.gauravd70.ecommerce.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.gauravd70.ecommerce.dtos.UserEntity;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, String>{

}
