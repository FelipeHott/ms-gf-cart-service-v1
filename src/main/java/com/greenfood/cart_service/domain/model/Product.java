package com.greenfood.cart_service.domain.model;

import lombok.Data;

@Data
public class Product {
    private String id;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer quantity;
}
