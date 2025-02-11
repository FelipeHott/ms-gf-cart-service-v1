package com.greenfood.cart_service.infrastructure.adapter.out.persistence;

import com.greenfood.cart_service.domain.model.ShoppingCart;
import com.greenfood.cart_service.application.ports.out.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartPersistenceAdapter implements CartRepository {
    private final SpringDataMongoCartRepository repository;
    private final CartMapper mapper;
    
    @Override
    public ShoppingCart save(ShoppingCart cart) {
        CartEntity entity = mapper.toEntity(cart);
        entity = repository.save(entity);
        return mapper.toDomain(entity);
    }
    
    @Override
    public Optional<ShoppingCart> findById(String id) {
        return repository.findById(id)
                .map(mapper::toDomain);
    }
    
    @Override
    public List<ShoppingCart> findByUserId(String userId) {
        return repository.findByUserId(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }
    
    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
    
    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }
}