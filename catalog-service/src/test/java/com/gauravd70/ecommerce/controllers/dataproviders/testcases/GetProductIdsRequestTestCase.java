package com.gauravd70.ecommerce.controllers.dataproviders.testcases;

import java.util.List;

import org.bson.types.ObjectId;

import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;
import com.gauravd70.ecommerce.dtos.requests.GetProductIdsRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class GetProductIdsRequestTestCase {
    private String familyId;
    private String variantId;
    private GetProductIdsRequest request;

    public static GetProductIdsRequestTestCase.Builder builder() {
        return new GetProductIdsRequestTestCase.Builder();
    }

    public static class Builder extends BaseTestCase {
        private List<ProductCatalogMappingDocument> mappings;
        private boolean isLastOffsetNull = true;

        private ProductCatalogMappingDocument getRandomProductCatalogMappingDocument() {
            return mappings.get(getRandomInt(1, mappings.size()) - 1);
        }

        private ObjectId getLastOffset(String familyId, String variantId) {
            List<ProductCatalogMappingDocument> filteredDocuments = mappings.stream()
                .filter(document -> document.getFamilyId().equals(familyId) && document.getVariantId().equals(variantId))
                .toList();

            if(filteredDocuments.size() == 0) {
                return null;
            }

            return filteredDocuments.get(filteredDocuments.size() / 2).getId();
        }

        public Builder isLastOffsetNull(boolean isLastOffsetNull) {
            this.isLastOffsetNull = isLastOffsetNull;
            return this;
        }

        public Builder mappings(List<ProductCatalogMappingDocument> mappings) {
            this.mappings = mappings;
            return this;
        }

        public GetProductIdsRequestTestCase build() {
            ProductCatalogMappingDocument document = getRandomProductCatalogMappingDocument();

            GetProductIdsRequest request;

            if(isLastOffsetNull) {
                request = GetProductIdsRequest.builder().variantId(document.getVariantId()).build();
            } else {
                request = GetProductIdsRequest
                    .builder()
                    .variantId(document.getVariantId())
                    .lastOffset(getLastOffset(document.getFamilyId(), document.getVariantId()).toString())
                    .build();
            }

            return new GetProductIdsRequestTestCase(document.getFamilyId(), document.getVariantId(), request);
        }
    }
}
