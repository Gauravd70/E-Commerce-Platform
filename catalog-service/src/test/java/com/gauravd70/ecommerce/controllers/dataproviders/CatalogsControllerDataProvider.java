package com.gauravd70.ecommerce.controllers.dataproviders;

import java.util.List;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.instancio.Instancio;
import org.instancio.Select;

import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;

public class CatalogsControllerDataProvider {
    public static List<ObjectId> categoryIds = Instancio.ofList(ObjectId.class).size(10).create();

    public static List<String> familyIds = Instancio.ofList(String.class).size(50).create();

    public static List<CatalogDocument> catalogs = Instancio.ofList(CatalogDocument.class)
            .size(500)
            .supply(Select.field(CatalogDocument::getCategoryId), random -> random.oneOf(categoryIds).toString())
            .supply(Select.field(CatalogDocument::getFamilyId), random -> familyIds.get(random.intRange(0, familyIds.size() - 1)))
            .create();

    public static int getRandomInt(int start, int end) {
        return Instancio.gen().ints().range(start, end).get();
    }

    public static ObjectId getRandomCategoryId() {
        return categoryIds.get(getRandomInt(1, categoryIds.size()) - 1);
    }

    public static ObjectId getRandomLastOffsetIdByCategoryId(String categoryId) {
        List<CatalogDocument> filteredCatalogs = catalogs
            .stream()
            .filter(document -> document.getCategoryId().equals(categoryId))
            .toList();

        if(filteredCatalogs.size() == 0) {
            return null;
        }

        return filteredCatalogs.get(getRandomInt(1, filteredCatalogs.size()) - 1).getId();        
    }

    public static Stream<GetCatalogsRequestTestCase> getCatalogRequestTestCases() {
        return Stream.of(
            GetCatalogsRequestTestCase.builder().categoryId(getRandomCategoryId()).lastOffsetisNull(true).build(),
            GetCatalogsRequestTestCase.builder().categoryId(getRandomCategoryId()).lastOffsetisNull(false).build()
        );
    }
}
