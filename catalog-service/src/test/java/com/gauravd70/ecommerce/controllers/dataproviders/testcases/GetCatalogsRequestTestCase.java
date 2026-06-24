package com.gauravd70.ecommerce.controllers.dataproviders.testcases;

import java.util.List;

import org.bson.types.ObjectId;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class GetCatalogsRequestTestCase {
    private String categoryId;
    private GetCatalogsRequest request;
    
    public static GetCatalogsRequestTestCase.Builder builder() {
        return new GetCatalogsRequestTestCase.Builder();
    }

    public static class Builder extends BaseTestCase {
        private List<CatalogDocument> catalogs;
        private List<String> categoryIds;
        private boolean lastOffsetisNull = true;

        private String getRandomCategoryId() {
            return categoryIds.get(getRandomInt(1, categoryIds.size()) - 1);
        }

        private ObjectId getLastOffset(String categoryId) {
            List<CatalogDocument> filteredCatalogs = catalogs
                .stream()
                .filter(document -> document.getCategoryId().equals(categoryId))
                .toList();

            if(filteredCatalogs.size() == 0) {
                return null;
            }

            return filteredCatalogs.get(filteredCatalogs.size() / 2).getId();        
        }

        public Builder catalogs(List<CatalogDocument> catalogs) {
            this.catalogs = catalogs;
            return this;
        }

        public Builder categoryIds(List<String> categoryIds) {
            this.categoryIds = categoryIds;
            return this;
        }

        public Builder lastOffsetisNull(boolean lastOffsetisNull) {
            this.lastOffsetisNull = lastOffsetisNull;
            return this;
        }

        public GetCatalogsRequestTestCase build() {
            String categoryId = getRandomCategoryId();

            GetCatalogsRequest request;

            if(lastOffsetisNull) {
                request = GetCatalogsRequest.builder().build();
            } else {
                request = GetCatalogsRequest.builder().lastOffset(getLastOffset(categoryId).toString()).build();
            }

            return new GetCatalogsRequestTestCase(categoryId, request);
        }
    }
}
