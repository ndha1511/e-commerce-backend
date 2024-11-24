package com.code.ecommercebackend.repositories.customizations.productFeature;

import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductFeatureRepositoryImpl implements ProductFeatureRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<CategoryCount> findLargestCategoryGroups(long userId) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("user_id").is(userId)),
                Aggregation.group("category")
                        .count().as("count"),
                Aggregation.project("count")
                        .and("_id").as("category")
        );

        AggregationResults<CategoryCount> results = mongoTemplate.aggregate(
                aggregation,
                "product_features",
                CategoryCount.class
        );
        return results.getMappedResults();
    }
}
