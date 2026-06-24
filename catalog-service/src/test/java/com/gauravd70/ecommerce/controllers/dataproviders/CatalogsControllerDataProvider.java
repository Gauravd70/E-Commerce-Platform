package com.gauravd70.ecommerce.controllers.dataproviders;

import java.util.List;
import java.util.stream.Stream;

import org.bson.types.ObjectId;
import org.instancio.Instancio;
import org.instancio.Select;

import com.gauravd70.ecommerce.controllers.dataproviders.testcases.GetCatalogsRequestTestCase;
import com.gauravd70.ecommerce.controllers.dataproviders.testcases.GetProductIdsRequestTestCase;
import com.gauravd70.ecommerce.dtos.documents.CatalogDocument;
import com.gauravd70.ecommerce.dtos.documents.ProductCatalogMappingDocument;

public class CatalogsControllerDataProvider {
    public static List<String> categoryIds = Instancio
        .ofList(String.class)
        .size(5)
        .generate(Select.all(String.class), gen -> gen.text().pattern("category_#a#a#a#a#a#a#a#a#a#a"))
        .create();

    public static List<String> familyIds = Instancio
        .ofList(String.class)
        .size(25)
        .generate(Select.all(String.class), gen -> gen.text().pattern("family_#a#a#a#a#a#a#a#a#a#a"))
        .create();

    public static List<CatalogDocument> catalogs = Instancio
        .ofList(CatalogDocument.class)
        .size(125)
        .supply(Select.field(CatalogDocument::getCategoryId), random -> random.oneOf(categoryIds).toString())
        .supply(Select.field(CatalogDocument::getFamilyId), random -> random.oneOf(familyIds))
        .generate(Select.field(CatalogDocument::getVariantId), gen -> gen.text().pattern("variant_#a#a#a#a#a#a#a#a#a#a"))
        .create();

    public static List<ProductCatalogMappingDocument> productCatalogMappings = Instancio
        .ofList(ProductCatalogMappingDocument.class)
        .size(625)
        .supply(Select.all(ProductCatalogMappingDocument.class), random -> {
            CatalogDocument catalog = random.oneOf(catalogs);

            return Instancio.of(ProductCatalogMappingDocument.class)
                .supply(Select.field(ProductCatalogMappingDocument::getFamilyId), () -> catalog.getFamilyId())
                .supply(Select.field(ProductCatalogMappingDocument::getVariantId), () -> catalog.getVariantId())
                .create();
        })
        .create();

    public static Stream<GetCatalogsRequestTestCase> getCatalogRequestTestCases() {
        return Stream.of(
            GetCatalogsRequestTestCase.builder().catalogs(catalogs).categoryIds(categoryIds).build(),
            GetCatalogsRequestTestCase.builder().catalogs(catalogs).categoryIds(categoryIds).lastOffsetisNull(false).build()
        );
    }

    public static Stream<GetProductIdsRequestTestCase> getProductIdsRequestTestCases() {
        return Stream.of(
            GetProductIdsRequestTestCase.builder().mappings(productCatalogMappings).build(),
            GetProductIdsRequestTestCase.builder().mappings(productCatalogMappings).isLastOffsetNull(false).build()
        );
    }
}
