package com.greenfood.cart_service.infrastructure.adapter.out.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface SpringDataMongoCartRepository extends MongoRepository<CartEntity, String> {
    List<CartEntity> findByUserId(String userId);
}
