package com.greenfood.cart_service.application.ports.out;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.greenfood.cart_service.domain.model.ShoppingCart;

public interface ShippingCalculator {
    Double calculateShipping(ShoppingCart cart, Address address);
    
} 
