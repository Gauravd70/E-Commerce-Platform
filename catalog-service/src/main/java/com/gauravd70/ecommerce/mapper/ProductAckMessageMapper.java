package com.gauravd70.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.messages.ProductAckMessage;
import com.gauravd70.ecommerce.dtos.messages.ProductActionsMessage;

@Mapper(componentModel = "spring")
public interface ProductAckMessageMapper {
    @Mapping(target = "createdAt", source = "productActionsMessage.createdAt")
    public ProductAckMessage toProductAckMessage(CatalogDocument catalogDocument, ProductActionsMessage productActionsMessage);
}
