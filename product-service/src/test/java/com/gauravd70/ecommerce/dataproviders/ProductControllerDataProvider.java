package com.gauravd70.ecommerce.dataproviders;

import java.util.List;
import java.util.stream.Stream;

import com.gauravd70.ecommerce.dtos.requests.ImageInfoRequest;
import com.gauravd70.ecommerce.dtos.requests.PostProductRequest;

public class ProductControllerDataProvider {

    public static Stream<PostProductRequest> getInvalidPostProductRequests() {
        return Stream.of(
            PostProductRequest.builder().build(), 
            PostProductRequest.builder().name("Apple MacBook Pro").build(),
            PostProductRequest.builder().name("Apple MacBook Pro").price(-1).build(),
            PostProductRequest.builder().name("Apple MacBook Pro").price(150000).quantity(-1).build(),
            PostProductRequest.builder().name("Apple MacBook Pro").price(150000).quantity(10).description("").build(),
            PostProductRequest.builder().name("Apple MacBook Pro").price(150000).quantity(10).description("M5 16 inch apple laptop").images(List.of(ImageInfoRequest.builder().build())).build(),
            PostProductRequest.builder().name("Apple MacBook Pro").price(150000).quantity(10).description("M5 16 inch apple laptop").images(List.of(ImageInfoRequest.builder().url("https://www.image.com").build())).build(),
            PostProductRequest.builder().name("Apple MacBook Pro").price(150000).quantity(10).description("M5 16 inch apple laptop").images(List.of(ImageInfoRequest.builder().url("https://www.image.com").type("thumbnail").displayOrder(-1).build())).build()
        );
    }
}
