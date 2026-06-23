package com.gauravd70.ecommerce.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;
import com.gauravd70.ecommerce.dtos.requests.GetProductIdsRequest;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogsResponse;
import com.gauravd70.ecommerce.dtos.responses.GetProductIdsResponse;
import com.gauravd70.ecommerce.services.CatalogsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CatalogsController {
    private final CatalogsService catalogsService;

    @GetMapping(value = "/{categoryId}")
    public GetCatalogsResponse getCatalogs(@PathVariable("categoryId") String categoryId, GetCatalogsRequest getCatalogsRequest) throws BadRequestException {
        return catalogsService.getCatalogs(categoryId, getCatalogsRequest);
    }

    @GetMapping(value = "ids/{familyId}")
    public GetProductIdsResponse GetProductIds(@PathVariable("familyId") String familyId, GetProductIdsRequest getProductIdsRequest) {
        return catalogsService.getProductIds(familyId, getProductIdsRequest);
    }
}
