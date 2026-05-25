package com.gauravd70.ecommerce.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfoResponse {
    private String id;
    private String url;
    private String type;
    private int displayOrder;
}
