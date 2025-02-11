package com.greenfood.cart_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItem {
    private String productId;
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer quantity;

    public Double getSubtotal(){
        return quantity * price;
    }
    
}


