package com.greenfood.cart_service.infrastructure.adapter.out.messaging;

import com.greenfood.cart_service.domain.enums.PaymentMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greenfood.cart_service.application.ports.out.*;
import com.greenfood.cart_service.domain.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaCartEventPublisher implements CartEventPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    private static final String CART_UPDATED_TOPIC = "cart-updates";
    private static final String CART_CREATED_TOPIC = "cart-created";
    private static final String CART_DELETED_TOPIC = "cart-deleted";
    private static final String CART_TOPIC = "cart-events";
    
    @PostConstruct
    public void setup() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void publishCartUpdated(ShoppingCart cart) {
        kafkaTemplate.send(CART_UPDATED_TOPIC, cart.getId(), cart);
    }
    
    @Override
    public void publishCartCreated(ShoppingCart cart) {
        kafkaTemplate.send(CART_CREATED_TOPIC, cart.getId(), cart);
    }
    
    @Override
    public void publishCartDeleted(String cartId) {
        kafkaTemplate.send(CART_DELETED_TOPIC, cartId);
    }

    @Override
    public void publishCartCheckout(ShoppingCart cart, PaymentMethod paymentMethod) {
        try {
            CartCheckoutEvent event = buildCartEvent(cart, paymentMethod);
            String message = objectMapper.writer()
                .withDefaultPrettyPrinter()
                .writeValueAsString(event)
                .replace("\\r\\n", "\n")
                .replace("\\", "");
            
            kafkaTemplate.send(CART_TOPIC, cart.getId(), message);
            log.info("Carrinho publicado no tópico {} com ID: {}", CART_TOPIC, cart.getId());
            
        } catch (JsonProcessingException e) {
            log.error("Erro ao serializar carrinho para Kafka: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao publicar evento do carrinho", e);
        }
    }
    
    private CartCheckoutEvent buildCartEvent(ShoppingCart cart, PaymentMethod paymentMethod) {
        return CartCheckoutEvent.builder()
                .userId(cart.getUserId()) // Corrigido: Usando getUserId ao invés de getId
                .totalAmount(cart.getTotal())
                .productQuantity(cart.getTotalItems())
                .paymentMethod(paymentMethod)
                .products(convertCartItemsToProducts(cart.getItems()))
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