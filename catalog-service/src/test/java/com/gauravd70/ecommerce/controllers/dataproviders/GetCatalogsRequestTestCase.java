package com.gauravd70.ecommerce.controllers.dataproviders;

import org.bson.types.ObjectId;

import com.gauravd70.ecommerce.dtos.requests.GetCatalogsRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCatalogsRequestTestCase {
    private ObjectId categoryId;
    private boolean lastOffsetisNull;
    
    public GetCatalogsRequest getRequest() {
        if(lastOffsetisNull) {
            return GetCatalogsRequest.builder().build();
        }

        return GetCatalogsRequest
            .builder()
            .lastOffset(
                CatalogsControllerDataProvider
                    .getRandomLastOffsetIdByCategoryId(categoryId.toString())
                    .toString()
                )
            .build();
    }
}
