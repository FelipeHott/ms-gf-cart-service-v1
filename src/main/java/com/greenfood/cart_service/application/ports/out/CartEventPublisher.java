package com.greenfood.cart_service.application.ports.out;

import com.greenfood.cart_service.domain.enums.PaymentMethod;
import com.greenfood.cart_service.domain.model.ShoppingCart;

public interface CartEventPublisher {
    void publishCartUpdated(ShoppingCart cart); //publicacao dos eventos
    void publishCartCreated(ShoppingCart cart);
    void publishCartDeleted(String cartId);
    void publishCartCheckout(ShoppingCart cart, PaymentMethod paymentMethod);
    
}
