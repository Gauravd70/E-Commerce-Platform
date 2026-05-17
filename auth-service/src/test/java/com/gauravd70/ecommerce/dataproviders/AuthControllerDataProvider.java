package com.gauravd70.ecommerce.dataproviders;

import java.util.stream.Stream;

import com.gauravd70.ecommerce.dtos.LoginRequest;

public class AuthControllerDataProvider {
    public static Stream<LoginRequest> invalidLoginRequest() {
        return Stream.of(LoginRequest.builder().username(null).password("abc123").build(),
                        LoginRequest.builder().username("abc123").password(null).build(),
                        LoginRequest.builder().username(null).password(null).build());
    }
}
