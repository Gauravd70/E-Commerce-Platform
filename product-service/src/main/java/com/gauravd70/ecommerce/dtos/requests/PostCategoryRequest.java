package com.gauravd70.ecommerce.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostCategoryRequest {
    @NotBlank
    private String name;
    @Builder.Default
    private boolean active = false;
}
