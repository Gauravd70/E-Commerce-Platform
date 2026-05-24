package com.gauravd70.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gauravd70.ecommerce.dtos.SignUpRequest;
import com.gauravd70.ecommerce.dtos.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public UserEntity toUserEntity(SignUpRequest request, String encodedPassword);
}
