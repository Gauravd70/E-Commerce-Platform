package com.gauravd70.ecommerce.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gauravd70.commons.dtos.GenericResponse;
import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.PatchCategoryRequest;
import com.gauravd70.ecommerce.dtos.requests.PostCategoryRequest;
import com.gauravd70.ecommerce.dtos.responses.CategoryInfoResponse;
import com.gauravd70.ecommerce.dtos.responses.GetCategoriesResponse;
import com.gauravd70.ecommerce.services.CategoriesService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories/v1")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoriesService categoriesService;

    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GenericResponse> onPostCategory(@Valid @RequestBody PostCategoryRequest request) throws BadRequestException {
        return categoriesService.createCategory(request);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public GetCategoriesResponse onGetCategories() {
        return categoriesService.getAllCategories();
    }

    @GetMapping(value = "/{categoryId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public CategoryInfoResponse onGetCategory(@PathVariable("categoryId") String categoryId) throws BadRequestException {
        return categoriesService.getCategory(categoryId);
    }

    @PatchMapping(value = "/{categoryId}", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    public GenericResponse onPatchCategory(@PathVariable("categoryId") String categoryId, @Valid @RequestBody PatchCategoryRequest request) throws BadRequestException {
        return categoriesService.patchCategory(categoryId, request);
    }

    @DeleteMapping(value = "/{categoryId}")
    public ResponseEntity<Void> onDeleteCategory(@PathVariable("categoryId") String categoryId) throws BadRequestException {
        return categoriesService.deleteCategory(categoryId);
    }
}
