package com.gauravd70.ecommerce.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCatalogsResponse {
    private List<GetCatalogDetails> catalogs;
}
