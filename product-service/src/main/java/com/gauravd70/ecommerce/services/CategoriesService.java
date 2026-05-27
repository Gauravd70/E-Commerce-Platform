package com.gauravd70.ecommerce.services;

import org.springframework.http.ResponseEntity;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.PatchCategoryRequest;
import com.gauravd70.ecommerce.dtos.requests.PostCategoryRequest;
import com.gauravd70.ecommerce.dtos.responses.CategoryInfoResponse;
import com.gauravd70.ecommerce.dtos.responses.GetCategoriesResponse;

public interface CategoriesService {
    /**
     * Create a new category
     * 
     * @param request {@link PostCategoryRequest}
     * @return {@link ResponseEntity}<{@link GenericResponse}>
     * @throws BadRequestException
     */
    public ResponseEntity<GenericResponse> createCategory(PostCategoryRequest request) throws BadRequestException;

    /**
     * Get a list of all the categories
     * 
     * @return {@link GetCategoriesResponse}
     */
    public GetCategoriesResponse getAllCategories();

    /**
     * Get a single category
     * 
     * @param categoryId {@link String}
     * @return {@link CategoryInfoResponse}
     * @throws BadRequestException
     */
    public CategoryInfoResponse getCategory(String categoryId) throws BadRequestException;

    /**
     * 
     * 
     * @param categoryId {@link String}
     * @param request {@link PatchCategoryRequest}
     * @return {@link GenericResponse}
     * @throws BadRequestException
     */
    public GenericResponse patchCategory(String categoryId, PatchCategoryRequest request) throws BadRequestException;

    /**
     * Delete a category
     * 
     * @param categoryId {@link String}
     * @return {@link ResponseEntity}<{@link Void}>
     * @throws BadRequestException
     */
    public ResponseEntity<Void> deleteCategory(String categoryId) throws BadRequestException;
}
