package com.greenfood.cart_service.domain.dto;


import com.greenfood.cart_service.domain.enums.PaymentMethod;
import com.greenfood.cart_service.domain.model.*;
import lombok.Data;
import lombok.Builder;
import java.util.List;

@Data
@Builder
public class CheckoutResponseDTO {
    private String userId;
    private Double totalAmount;
    private Integer productQuantity;
    private PaymentMethod paymentMethod;
    private List<Product> products;
    private String cartId;
    private String checkoutDate;
}
