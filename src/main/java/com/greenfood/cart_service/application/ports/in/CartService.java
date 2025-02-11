package com.greenfood.cart_service.application.ports.in;

import com.greenfood.cart_service.domain.dto.CheckoutResponseDTO;
import com.greenfood.cart_service.domain.enums.Cartstatus;
import com.greenfood.cart_service.domain.enums.PaymentMethod;
import com.greenfood.cart_service.domain.model.*;
import com.nawforce.runforce.ConnectApi.CartStatus;
import com.greenfood.cart_service.application.ports.out.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CartService implements ManageCartUseCase {
    private final CartRepository cartRepository;
    private final CartEventPublisher cartEventPublisher;
    

    @Override
    public ShoppingCart createCart(String userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(UUID.randomUUID().toString());
        cart.setUserId(userId);
        cart = cartRepository.save(cart);
        cartEventPublisher.publishCartCreated(cart);
        return cart;
    }

    @Override
    public ShoppingCart addItem(String cartId, CartItem item) {
        log.info("Buscando carrinho: {}", cartId);
        if (item == null) {
            throw new IllegalArgumentException("Item não pode ser nulo");
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
        if (item.getPrice() == null || item.getPrice() <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
        
        ShoppingCart cart = cartRepository.findById(cartId)
                .orElseThrow();
        
        log.info("Carrinho encontrado: {}", cart);
        cart.addItem(item);
        log.info("Item adicionado, salvando carrinho");
        
        return cartRepository.save(cart);
    }
    
    

    @Override
    public ShoppingCart updateItemQuantity(String cartId, String productId, Integer quantity) {
        ShoppingCart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.updateItemQuantity(productId, quantity);
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart removeItem(String cartId, String productId) {
        ShoppingCart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.removeItem(productId);
        return cartRepository.save(cart);
    }

    @Override
    public ShoppingCart getCart(String cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public Double calculateShipping(String cartId, AdressClient address) {
        ShoppingCart cart = getCart(cartId);
        Double shippingFee = 10.0; // Valor fixo para exemplo logica de calculo todo
        cart.setDeliveryAddress(address);
        cart.setShippingFee(shippingFee);
        cartRepository.save(cart);
        return shippingFee;
    }

    
    private void validateCheckout(ShoppingCart cart) {
       if (cart.isEmpty()) {
        throw new IllegalArgumentException("Não é possível finalizar um carrinho vazio");
       }

       if (cart.getStatus() != Cartstatus.ACTIVE) {
        throw new IllegalArgumentException("Carrinho não está ativo");
       }

       if (cart.getDeliveryAddress() == null) {
        throw new IllegalArgumentException("Endereço de entrega não informado");
       }

       if (cart.getShippingFee() == null || cart.getShippingFee() <= 0) {
        throw new IllegalArgumentException("Frete não calculado");
       }
   }


    @Override
    public ShoppingCart checkoutCart(String cartId, PaymentMethod paymentMethod) {
        log.info("Iniciando checkout do carrinho: {}", cartId);
    
    ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow();

    validateCheckout(cart);

    cart.setStatus(Cartstatus.COMPLETED);
    cart.setUpdatedAt(LocalDateTime.now());
    cart = cartRepository.save(cart);

    try {
        cartEventPublisher.publishCartCheckout(cart, paymentMethod);
        log.info("Evento de checkout publicado com sucesso para o carrinho: {}", cartId);
    } catch (Exception e) {
        log.error("Erro ao publicar evento de checkout do carrinho: {}", cartId, e);
        cart.setStatus(Cartstatus.ACTIVE);
        cartRepository.save(cart);
        throw new RuntimeException("Erro ao processar checkout", e);
    }

    return cart;
    }

    @Override
    public CheckoutResponseDTO getCheckoutInfo(String cartId) {
    ShoppingCart cart = cartRepository.findById(cartId)
            .orElseThrow();

    if (cart.getStatus() != Cartstatus.COMPLETED) {
        throw new IllegalArgumentException("Carrinho sem finalizar");
    }

    return CheckoutResponseDTO.builder()
            .userId(cart.getUserId())
            .totalAmount(cart.getTotal())
            .productQuantity(cart.getTotalItems())
            .products(convertCartItemsToProducts(cart.getItems()))
            .cartId(cart.getId())
            .checkoutDate(cart.getUpdatedAt().toString())
            .build();
    }


    private List<Product> convertCartItemsToProducts(List<CartItem> items) {
    if (items == null) {
        return new ArrayList<>();
    }
    return items.stream()
            .map(item -> {
                Product product = new Product();
                product.setId(item.getProductId());
                product.setName(item.getName());
                product.setDescription(item.getDescription());
                product.setPrice(item.getPrice());
                product.setCategory(item.getCategory());
                product.setQuantity(item.getQuantity());
                return product;
            })
            .collect(Collectors.toList());
}
}
