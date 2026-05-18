package com.gauravd70.ecommerce.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gauravd70.ecommerce.dtos.SignUpRequest;
import com.gauravd70.ecommerce.dtos.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "encodedPassword")
    public UserEntity toUserEntity(SignUpRequest request, List<String> roles, String encodedPassword);
}
