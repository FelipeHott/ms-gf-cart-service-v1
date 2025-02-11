package com.greenfood.cart_service.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.greenfood.cart_service.domain.enums.Cartstatus;

@Data
public class ShoppingCart {
    private String id;
    private String userId;
    private List<CartItem> items = new ArrayList<>();
    private AdressClient deliveryAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Double shippingFee;
    private Cartstatus status = Cartstatus.ACTIVE;
    


    public ShoppingCart() {
       this.items = new ArrayList<>();
       this.createdAt = LocalDateTime.now();
       this.updatedAt = LocalDateTime.now();
       this.status = Cartstatus.ACTIVE;  
       this.shippingFee = 0.0;
   }


   public Double getSubtotal() {
    if (items == null || items.isEmpty()) {
        return 0.0;
    }
    return items.stream()
            .mapToDouble(CartItem::getSubtotal)
            .sum();
}

  
    public Double getTotal() {
        return getSubtotal() + (shippingFee != null ? shippingFee : 0.0);
    }

    
    public void addItem(CartItem newItem) {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
 
        boolean itemExists = false;
        for (CartItem item : items) {
            if (item.getProductId().equals(newItem.getProductId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            items.add(newItem);
        }

        this.updatedAt = LocalDateTime.now();
    }
    

   
    public void updateItemQuantity(String productId, Integer quantity) {
        if (this.items == null) {
            throw new RuntimeException("Carrinho não possui itens");
        }
 
        items.stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.setQuantity(quantity);
                            this.updatedAt = LocalDateTime.now();
                        },
                        () -> {
                            throw new RuntimeException("Item não encontrado no carrinho");
                        }
                );
    }

    
    public void removeItem(String productId) {
        if (this.items == null) {
            throw new RuntimeException("Carrinho não possui itens");
        }
 
        boolean removed = items.removeIf(item -> item.getProductId().equals(productId));
        if (removed) {
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new RuntimeException("Item não encontrado no carrinho");
        }
    }

    public boolean isEmpty() {
        return items == null || items.isEmpty();
    }

    public Integer getTotalItems() {
        if (items == null) {
            return 0;
        }
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
    public void updateStatus(Cartstatus newStatus) {
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeliveryAddress(AdressClient address) {
        this.deliveryAddress = address;
        this.updatedAt = LocalDateTime.now();
    }

    public void setShippingFee(Double fee) {
        this.shippingFee = fee;
        this.updatedAt = LocalDateTime.now();
    }
}
