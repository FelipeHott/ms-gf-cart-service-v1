package com.greenfood.cart_service.domain.model;


import lombok.Data;
import lombok.Builder;
import java.util.List;
import com.greenfood.cart_service.domain.enums.PaymentMethod;


@Data
@Builder
public class CartCheckoutEvent {
    private String userId;
    private Double totalAmount;
    private Integer productQuantity;
    private PaymentMethod paymentMethod;
    private List<Product> products;
}