package com.gauravd70.ecommerce.mapper;

import org.mapstruct.Mapper;

import com.gauravd70.ecommerce.dtos.SignUpRequest;
import com.gauravd70.ecommerce.dtos.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    public UserEntity toUserEntity(SignUpRequest request);
}
