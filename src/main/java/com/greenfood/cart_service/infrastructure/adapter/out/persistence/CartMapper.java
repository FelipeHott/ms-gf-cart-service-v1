package com.greenfood.cart_service.infrastructure.adapter.out.persistence;



import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.greenfood.cart_service.domain.model.AdressClient;
import com.greenfood.cart_service.domain.model.CartItem;
import com.greenfood.cart_service.domain.model.ShoppingCart;


@Component
public class CartMapper {
    
    
    public CartEntity toEntity(ShoppingCart domain) {
        CartEntity entity = new CartEntity();
        entity.setId(domain.getId());
        entity.setUserId(domain.getUserId());
        
        
        List<CartItemEntity> itemEntities = new ArrayList<>();
        if (domain.getItems() != null) {
            itemEntities.addAll(domain.getItems().stream()
                    .map(this::toItemEntity)
                    .collect(Collectors.toList()));
        }
        entity.setItems(itemEntities);
        
        entity.setAddress(toAddressEntity(domain.getDeliveryAddress()));
        entity.setShippingFee(domain.getShippingFee());
        entity.setStatus(domain.getStatus());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(domain.getUpdatedAt());
        return entity;
    }
    
    public ShoppingCart toDomain(CartEntity entity) {
        ShoppingCart domain = new ShoppingCart();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        
       
        List<CartItem> items = new ArrayList<>();
        if (entity.getItems() != null) {
            items.addAll(entity.getItems().stream()
                    .map(this::toItemDomain)
                    .collect(Collectors.toList()));
        }
        domain.setItems(items);
        
        domain.setDeliveryAddress(toAddressDomain(entity.getAddress()));
        domain.setShippingFee(entity.getShippingFee());
        domain.setStatus(entity.getStatus());
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }
    
    private CartItemEntity toItemEntity(CartItem domain) {
        if (domain == null) return null;
        CartItemEntity entity = new CartItemEntity();
        entity.setProductId(domain.getProductId());
        entity.setQuantity(domain.getQuantity());
        entity.setPrice(domain.getPrice());
        return entity;
    }
    
    private CartItem toItemDomain(CartItemEntity entity) {
        if (entity == null) return null;
        CartItem item = new CartItem();
        item.setProductId(entity.getProductId());
        item.setQuantity(entity.getQuantity());
        item.setPrice(entity.getPrice());
        return item;
    }
    
    private AddressEntity toAddressEntity(AdressClient domain) {
        if (domain == null) return null;
        AddressEntity entity = new AddressEntity();
        entity.setStreet(domain.getStreet());
        entity.setNumber(domain.getNumber());
        entity.setComplement(domain.getComplement());
        entity.setNeighborhood(domain.getNeighborhood());
        entity.setCity(domain.getCity());
        entity.setState(domain.getState());
        entity.setZipCode(domain.getZipCode());
        return entity;
    }
    
    private AdressClient toAddressDomain(AddressEntity entity) {
        if (entity == null) return null;
        AdressClient address = new AdressClient();
        address.setStreet(entity.getStreet());
        address.setNumber(entity.getNumber());
        address.setComplement(entity.getComplement());
        address.setNeighborhood(entity.getNeighborhood());
        address.setCity(entity.getCity());
        address.setState(entity.getState());
        address.setZipCode(entity.getZipCode());
        return address;
    }
}