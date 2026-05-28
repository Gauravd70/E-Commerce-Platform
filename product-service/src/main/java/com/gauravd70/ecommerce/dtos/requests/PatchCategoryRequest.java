package com.gauravd70.ecommerce.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatchCategoryRequest {
    @NotEmpty
    private String name;
    private Boolean active;
}
