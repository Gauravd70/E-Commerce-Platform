package com.gauravd70.ecommerce.dtos.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_image_mappings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageMappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "product_id")
    private long productId;
    private String url;
    private String type;
    @Column(name = "display_order")
    private int displayOrder;
}
