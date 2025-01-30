package com.greenfood.cart_service.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails.Address;

import com.greenfood.cart_service.domain.model.Adress;
import com.greenfood.cart_service.domain.model.CartItem;


@Data
public class ShoppingCart {
    private String id;
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private Address deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double shippingFee;
    


    public Double getSubtotal() {
        return items.stream()
                .mapToDouble(CartItem::getSubtotal)
                .sum();
    }

    public Double getTotal() {
        return getSubtotal() + (shippingFee != null ? shippingFee : 0.0);
    }

    //adicionar ou atualizar produto no carrinho
    public void addItem(CartItem newItem) {
        items.stream()
            .filter(item -> item.getProdudctId().equals(newItem.getProdudctId()))
            .findFirst()
            .ifPresentOrElse(
                existingItem -> existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity()),
                () -> items.add(newItem)
            );
    }
    //atualizar a quantidade de um item no carrinho
    public void updateItemQuantity(String productId, Integer quantity) {
        items.stream()
            .filter(item -> item.getProdudctId().equals(productId))
            .findFirst()
            .ifPresent(item -> item.setQuantity(quantity));
    }
    //remover item 
    public void removeItem(String productId) {
        items.removeIf(item -> item.getProdudctId().equals(productId));
    }
}
