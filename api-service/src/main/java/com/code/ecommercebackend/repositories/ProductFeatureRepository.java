package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.ProductFeature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductFeatureRepository extends MongoRepository<ProductFeature, String> {
    Optional<ProductFeature> findByUserIdAndProductId(long userId, long productId);
    boolean existsByUserId(long userId);

    /**
     * Top 5 products with the highest views
     */
    List<ProductFeature> findTop5ByOrderByCountViewDesc();

    /**
     * 5 most recently viewed products by the user
     */
    List<ProductFeature> findTop5ByUserIdOrderByViewDateDesc(long userId);
    Optional<ProductFeature> findByProductIdAndUserIdIsNull(long productId);
    boolean existsByUserIdAndRatingIsNotNull(long userId);

}
