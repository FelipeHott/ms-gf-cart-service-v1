package com.greenfood.cart_service.application.ports.out;

import com.greenfood.cart_service.domain.model.ShoppingCart;
import java.util.Optional;
import java.util.List;

public interface  CartRepository {
    ShoppingCart save(ShoppingCart cart);
    Optional<ShoppingCart> findById(String id);
    List<ShoppingCart> findByUserId(String userId);
    void deleteById(String id);
    boolean existsById(String id);
}
