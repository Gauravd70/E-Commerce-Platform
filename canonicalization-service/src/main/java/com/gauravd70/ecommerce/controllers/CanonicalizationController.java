package com.gauravd70.ecommerce.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gauravd70.commons.exceptions.BadRequestException;
import com.gauravd70.ecommerce.dtos.GetCanonicalProductResponse;
import com.gauravd70.ecommerce.dtos.PostCanonicalProductRequest;
import com.gauravd70.ecommerce.dtos.PostCanonicalProductResponse;
import com.gauravd70.ecommerce.services.CanonicalizationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CanonicalizationController {
    private final CanonicalizationService canonicalizationService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public PostCanonicalProductResponse onPostCanonicalization(@Valid @RequestBody PostCanonicalProductRequest request) {
        return canonicalizationService.onCanonicalization(request);
    }

    @GetMapping(value = "/{groupId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public GetCanonicalProductResponse onGetCanonicalProduct(@PathVariable("groupId") String groupId) throws BadRequestException {
        return canonicalizationService.onGetCanonicalProduct(groupId);
    }
}
