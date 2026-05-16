package com.gauravd70.ecommerce.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginRequest {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
