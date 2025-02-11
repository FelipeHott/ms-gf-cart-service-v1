package com.greenfood.cart_service.application.ports.in;

import com.greenfood.cart_service.domain.model.CartItem;
import com.greenfood.cart_service.domain.model.ShoppingCart;
import com.greenfood.cart_service.domain.dto.CheckoutResponseDTO;
import com.greenfood.cart_service.domain.enums.PaymentMethod;
import com.greenfood.cart_service.domain.model.AdressClient;

public interface ManageCartUseCase {
    ShoppingCart createCart(String userId);
    ShoppingCart addItem(String cartId, CartItem item);
    ShoppingCart updateItemQuantity(String cartId, String productId, Integer quantity);
    ShoppingCart removeItem(String cartId, String productId);
    ShoppingCart getCart(String cartId);
    Double calculateShipping(String cartId, AdressClient address);
    ShoppingCart checkoutCart(String cartId, PaymentMethod paymentMethod);
    CheckoutResponseDTO getCheckoutInfo(String cartId);
      
}
