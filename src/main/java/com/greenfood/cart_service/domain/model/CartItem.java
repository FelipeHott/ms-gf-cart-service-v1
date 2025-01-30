package com.greenfood.cart_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CartItem {
    private String produdctId;
    private Integer quantity; 
    private Double price;

    public Double getSubtotal(){
        return quantity * price;
    }
    
}
