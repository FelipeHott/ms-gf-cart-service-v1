package com.greenfood.cart_service.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "cartitem")
public class CartItemEntity {
    private String productId;
    private Integer quantity;
    private Double price;
}
