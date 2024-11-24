package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.ProductFeature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductFeatureRepository extends MongoRepository<ProductFeature, String> {
    Optional<ProductFeature> findByUserIdAndProductId(long userId, long productId);
    boolean existsByUserId(long userId);
    ProductFeature findTopByOrderByCountViewDesc();
    ProductFeature findTopByUserIdOrderByCountViewDesc(Long userId);
    Optional<ProductFeature> findByProductIdAndUserIdIsNull(long productId);
    List<ProductFeature> findAllByCategoryAndUserId(String category, long userId);
}
