package com.gauravd70.ecommerce.dtos.messages;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryMessage {
    private String id;
    private String name;
    private List<String> variantAttributes;
}
