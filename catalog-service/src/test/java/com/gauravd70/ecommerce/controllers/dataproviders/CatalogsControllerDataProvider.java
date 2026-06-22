package com.gauravd70.ecommerce.controllers.dataproviders;

import java.util.List;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.instancio.Instancio;
import org.instancio.Select;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;

public class CatalogsControllerDataProvider {
    public static List<ObjectId> categoryIds = Instancio.ofList(ObjectId.class).size(3).create();

    public static List<CatalogDocument> catalogs = Instancio.ofList(CatalogDocument.class)
            .supply(Select.field(CatalogDocument::getCategoryId), random -> random.oneOf(categoryIds))
            .create();

    public static int getRandomInt(int start, int end) {
        return Instancio.gen().ints().range(start, end - 1).get();
    }

    public static ObjectId getRandomCategoryId() {
        return categoryIds.get(getRandomInt(0, categoryIds.size()));
    }

    public static ObjectId getRandomLastOffsetIdByCategoryId(String categoryId) {
        List<CatalogDocument> filteredCatalogs = catalogs
            .stream()
            .filter(document -> document.getCategoryId().equals(categoryId))
            .toList();

        return filteredCatalogs.get(getRandomInt(0, filteredCatalogs.size())).getId();        
    }

    public Stream<GetCatalogsRequestTestCase> getCatalogRequestTestCases() {
        return Stream.of(
            GetCatalogsRequestTestCase.builder().categoryId(getRandomCategoryId()).lastOffsetisNull(true).build(),
            GetCatalogsRequestTestCase.builder().categoryId(getRandomCategoryId()).lastOffsetisNull(false).build()
        );
    }
}
