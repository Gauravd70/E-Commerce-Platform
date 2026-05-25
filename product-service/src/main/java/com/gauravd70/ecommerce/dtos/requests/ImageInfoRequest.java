package com.gauravd70.ecommerce.dtos.requests;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfoRequest {
    @URL
    private String url;
    @NotBlank
    @Pattern(regexp = "(thumbnail|gallery)")
    private String type;
    @PositiveOrZero
    private int displayOrder;
}
