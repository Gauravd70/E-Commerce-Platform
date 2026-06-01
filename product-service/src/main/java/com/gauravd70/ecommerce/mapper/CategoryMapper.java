package com.gauravd70.ecommerce.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.gauravd70.ecommerce.dtos.documents.CategoryDocument;
import com.gauravd70.ecommerce.dtos.messages.CategoryMessage;
import com.gauravd70.ecommerce.dtos.requests.PatchCategoryRequest;
import com.gauravd70.ecommerce.dtos.requests.PostCategoryRequest;
import com.gauravd70.ecommerce.dtos.responses.CategoryInfoResponse;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends BaseMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    public CategoryDocument toCategoryDocument(PostCategoryRequest request);

    CategoryInfoResponse toCategoryInfoResponse(CategoryDocument document);

    List<CategoryInfoResponse> toCategoryInfoResponseList(Iterable<CategoryDocument> iterable);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CategoryDocument toCategoryDocument(@MappingTarget CategoryDocument originalDocument, PatchCategoryRequest request);

    CategoryMessage toCategoryMessage(CategoryDocument document);
}
