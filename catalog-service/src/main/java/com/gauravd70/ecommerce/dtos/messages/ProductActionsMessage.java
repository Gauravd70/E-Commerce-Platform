package com.gauravd70.ecommerce.dtos.messages;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductActionsMessage {
    private String id;
    private String brand;
    private String model;
    private Map<String, String> attributes;
    private CategoryMessage category;
    private String action;
    private long createdAt;
    private int version;
}
