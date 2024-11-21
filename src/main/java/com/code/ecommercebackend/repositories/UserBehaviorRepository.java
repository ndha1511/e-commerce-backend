package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.ProductFeature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserBehaviorRepository extends MongoRepository<ProductFeature, String> {
    Optional<ProductFeature> findByUserIdAndProductId(long userId, long productId);
    boolean existsByUserId(long userId);
}
