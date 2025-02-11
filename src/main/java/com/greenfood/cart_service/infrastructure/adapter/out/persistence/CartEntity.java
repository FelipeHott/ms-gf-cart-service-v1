package com.greenfood.cart_service.infrastructure.adapter.out.persistence;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.greenfood.cart_service.domain.enums.Cartstatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "carts")
public class CartEntity {
    @Id
    private String id;
    private String userId;
    private List<CartItemEntity> items = new ArrayList<>();  // Corrigido para CartItemEntity
    private AddressEntity address;
    private Double shippingFee;
    private Cartstatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}