package com.gauravd70.ecommerce.services;

import org.bson.types.ObjectId;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.documents.CategoryDocument;
import com.gauravd70.ecommerce.dtos.requests.PatchCategoryRequest;
import com.gauravd70.ecommerce.dtos.requests.PostCategoryRequest;
import com.gauravd70.ecommerce.dtos.responses.CategoryInfoResponse;
import com.gauravd70.ecommerce.dtos.responses.GetCategoriesResponse;
import com.gauravd70.ecommerce.mapper.CategoryMapper;
import com.gauravd70.ecommerce.repositories.CategoriesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public ResponseEntity<GenericResponse> createCategory(PostCategoryRequest request) throws BadRequestException {
        CategoryDocument categoryDocument = categoryMapper.toCategoryDocument(request);

        try {
            categoryDocument = categoriesRepository.save(categoryDocument);
        } catch(DataIntegrityViolationException e) {
            throw new BadRequestException();
        }

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(GenericResponse.builder().id(categoryDocument.getId().toString()).message("Category created successfully").build());
    }

    @Override
    public GetCategoriesResponse getAllCategories() {
        Iterable<CategoryDocument> iterable = categoriesRepository.findAll();

        return GetCategoriesResponse.builder().categories(categoryMapper.toCategoryInfoResponseList(iterable)).build();
    }

    @Override
    public CategoryInfoResponse getCategory(String categoryId) throws BadRequestException {
        ObjectId id;

        try {
            id = categoryMapper.toObjectId(categoryId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        CategoryDocument categoryDocument = categoriesRepository.findById(id).orElseThrow(() -> new BadRequestException());

        return categoryMapper.toCategoryInfoResponse(categoryDocument);
    }

    @Override
    public GenericResponse patchCategory(String categoryId, PatchCategoryRequest request) throws BadRequestException {
        ObjectId id;

        try {
            id = categoryMapper.toObjectId(categoryId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        CategoryDocument document = categoriesRepository.findById(id).orElseThrow(() -> new BadRequestException());

        CategoryDocument updatedDocument = categoryMapper.toCategoryDocument(document, request);

        try {
            categoriesRepository.save(updatedDocument);
        } catch(DataIntegrityViolationException e) {
            throw new BadRequestException();
        }

        return GenericResponse.builder().message("Category updated successfully").build();
    }

    @Override
    public ResponseEntity<Void> deleteCategory(String categoryId) throws BadRequestException {
        ObjectId id;

        try {
            id = categoryMapper.toObjectId(categoryId);
        } catch(Exception e) {
            throw new BadRequestException();
        }

        categoriesRepository.deleteById(id);

        return ResponseEntity.ok().build();
    }

}
