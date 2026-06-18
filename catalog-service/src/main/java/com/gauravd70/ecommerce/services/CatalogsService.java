package com.gauravd70.ecommerce.services;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;
import com.gauravd70.ecommerce.dtos.responses.GetCatalogsResponse;

public interface CatalogsService {
    /**
     * Get all the catalogs as per the request query params
     * 
     * @param categoryId {@link String}
     * @param request {@link GetCatalogsRequest}
     * @return {@link GetCatalogsResponse}
     * @throws BadRequestException
     */
    public GetCatalogsResponse getCatalogs(String categoryId, GetCatalogsRequest request) throws BadRequestException;
}
