package com.gauravd70.ecommerce.services;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.CanonicalProductDocument;
import com.gauravd70.ecommerce.dtos.GetCanonicalProductResponse;
import com.gauravd70.ecommerce.dtos.PostCanonicalProductRequest;
import com.gauravd70.ecommerce.dtos.PostCanonicalProductResponse;
import com.gauravd70.ecommerce.mapper.CanonicalProductMapper;
import com.gauravd70.ecommerce.mapper.NormalizationMapper;
import com.gauravd70.ecommerce.repositories.CanonicalProductsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CanonicalizationServiceImpl implements CanonicalizationService {
    private final CanonicalProductsRepository canonicalProductsRepository;
    private final CanonicalProductMapper canonicalProductMapper;
    private final NormalizationMapper normalizationMapper;

    @Override
    public PostCanonicalProductResponse onCanonicalization(PostCanonicalProductRequest request) {
        PostCanonicalProductRequest normalizedRequest = PostCanonicalProductRequest.builder().name(normalizationMapper.normalize(request.getName())).attributes(normalizationMapper.normalize(request.getAttributes())).build();

        return PostCanonicalProductResponse.builder().groupId(null).build();
    }

    @Override
    public GetCanonicalProductResponse onGetCanonicalProduct(String groupId) throws BadRequestException {
        CanonicalProductDocument document = canonicalProductsRepository.findByGroupId(groupId).orElseThrow(() -> new BadRequestException());

        return canonicalProductMapper.tGetCanonicalProductResponse(document);
    }
}
