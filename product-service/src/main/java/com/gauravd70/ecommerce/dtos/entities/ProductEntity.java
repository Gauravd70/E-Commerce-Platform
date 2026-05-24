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
@Table(name = "products")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private double price;
    private String description;
    private String attributes;
    @Column(name = "seller_id")
    private long sellerId;
    @Column(name = "group_id")
    private long groupId;
    @Column(insertable = false)
    private boolean active;
    @Column(insertable = false)
    private long createdAt;
    @Column(insertable = false)
    private long updatedAt;
}
