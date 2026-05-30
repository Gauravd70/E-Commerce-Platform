package com.gauravd70.ecommerce.services;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.GetCanonicalProductResponse;
import com.gauravd70.ecommerce.dtos.PostCanonicalProductRequest;
import com.gauravd70.ecommerce.dtos.PostCanonicalProductResponse;

public interface CanonicalizationService {
    /**
     * Canonicalize the product 
     * 
     * @param request {@link PostCanonicalProductRequest}
     * @return {@link PostCanonicalProductResponse}
     */
    public PostCanonicalProductResponse onCanonicalization(PostCanonicalProductRequest request);

    /**
     * Get canonical product for the given group Id
     * 
     * @param groupId {@link String}
     * @return {@link GetCanonicalProductResponse}
     * @throws BadRequestException 
     */
    public GetCanonicalProductResponse onGetCanonicalProduct(String groupId) throws BadRequestException;
}
