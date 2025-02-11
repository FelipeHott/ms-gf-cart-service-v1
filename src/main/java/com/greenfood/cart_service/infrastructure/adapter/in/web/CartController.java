package com.greenfood.cart_service.infrastructure.adapter.in.web;

import com.greenfood.cart_service.domain.dto.CheckoutResponseDTO;
import com.greenfood.cart_service.domain.enums.PaymentMethod;
import com.greenfood.cart_service.domain.model.*;
import com.greenfood.cart_service.application.ports.in.InvalidCartOperationException;
import com.greenfood.cart_service.application.ports.in.ManageCartUseCase;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrinho")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final ManageCartUseCase manageCartUseCase;
    
    @PostMapping
    public ResponseEntity<ShoppingCart> createCart(@RequestParam(required = true) String userId) {
        return ResponseEntity.ok(manageCartUseCase.createCart(userId));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<ShoppingCart> getCart(@PathVariable String cartId) {
        return ResponseEntity.ok(manageCartUseCase.getCart(cartId));
    }
    
    @PostMapping("/{cartId}/items")
    public ResponseEntity<ShoppingCart> addItem(
            @PathVariable String cartId,
            @RequestBody CartItem item) {
        log.info("Recebendo requisição para adicionar item. CartId: {}, Item: {}", cartId, item);
        try {
            ShoppingCart cart = manageCartUseCase.addItem(cartId, item);
            log.info("Item adicionado com sucesso ao carrinho {}", cartId);
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            log.error("Erro ao adicionar item ao carrinho {}: {}", cartId, e.getMessage(), e);
            throw e;
        }
    }
    
    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<ShoppingCart> updateItemQuantity(
            @PathVariable String cartId,
            @PathVariable String productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(manageCartUseCase.updateItemQuantity(cartId, productId, quantity));
    }
    
    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<ShoppingCart> removeItem(
            @PathVariable String cartId,
            @PathVariable String productId) {
        return ResponseEntity.ok(manageCartUseCase.removeItem(cartId, productId));
    }
    
    @PostMapping("/{cartId}/frete")
    public ResponseEntity<Double> calculateShipping(
            @PathVariable String cartId,
            @RequestBody AdressClient address) {
        return ResponseEntity.ok(manageCartUseCase.calculateShipping(cartId, address));
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<ShoppingCart> checkoutCart(
            @PathVariable String cartId,
            @RequestBody CheckoutRequest request) {
        try {
            return ResponseEntity.ok(manageCartUseCase.checkoutCart(cartId, request.getPaymentMethod()));
        } catch (Exception e) {
            log.error("Erro no checkout do carrinho {}: {}", cartId, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
     @GetMapping("/{cartId}/checkout/info")
        public ResponseEntity<CheckoutResponseDTO> getCheckoutInfo(@PathVariable String cartId) {
        try {
            log.info("Buscando informações de checkout para o carrinho: {}", cartId);
            CheckoutResponseDTO checkoutInfo = manageCartUseCase.getCheckoutInfo(cartId);
            return ResponseEntity.ok(checkoutInfo);
        } catch (Exception e) {
            log.error("Carrinho não encontrado: {}", cartId);
            return ResponseEntity.notFound().build();
        } 
    }
}

//Depois mover para uma outra classe
@Data
class CheckoutRequest {
    private PaymentMethod paymentMethod;
}