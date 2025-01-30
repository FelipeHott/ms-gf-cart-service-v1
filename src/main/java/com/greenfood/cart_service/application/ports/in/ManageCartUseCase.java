package com.greenfood.cart_service.application.ports.in;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.greenfood.cart_service.domain.model.CartItem;
import com.greenfood.cart_service.domain.model.ShoppingCart;

public interface ManageCartUseCase {
     ShoppingCart createCart(String userId);  
    ShoppingCart addItem(String cartId, CartItem item);  
    ShoppingCart updateItemQuantity(String cartId, String productId, Integer quantity);  // Atualiza quantidade
    ShoppingCart removeItem(String cartId, String productId);  // Remove item
    ShoppingCart getCart(String cartId);
    Double calculateShipping(String cartId, Address address);  
}
