package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.ProductFeature;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductFeatureRepository extends MongoRepository<ProductFeature, String> {
    Optional<ProductFeature> findByUserIdAndProductId(long userId, long productId);
    boolean existsByUserId(long userId);

    /**
     * Top 5 products with the highest views
     */
    @Aggregation(pipeline = {
            "{ $sort: { countView: -1 } }",
            "{ $group: { _id: '$category', product: { $first: '$$ROOT' } } }",
            "{ $replaceRoot: { newRoot: '$product' } }",
            "{ $sort: { countView: -1 } }",
            "{ $limit: 5 }"
    })
    List<ProductFeature> findTop5ByOrderByCountViewDesc();

    /**
     * 5 most recently viewed products by the user
     */
    @Aggregation(pipeline = {
            "{ $match: { userId: ?0 } }",
            "{ $sort: { viewDate: -1 } }",
            "{ $group: { _id: '$category', product: { $first: '$$ROOT' } } }",
            "{ $replaceRoot: { newRoot: '$product' } }",
            "{ $sort: { viewDate:  -1} }",
            "{ $limit: 5 }"
    })
    List<ProductFeature> findTop5ByUserIdOrderByViewDateDesc(long userId);
    Optional<ProductFeature> findByProductIdAndUserIdIsNull(long productId);
    boolean existsByUserIdAndRatingIsNotNull(long userId);

}
