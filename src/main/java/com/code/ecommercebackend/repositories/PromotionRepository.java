package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.models.enums.PromotionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface PromotionRepository extends MongoRepository<Promotion, String> {
    @Query("{ 'promotionType': ?0, 'startDate': { $lt: ?#{T(java.time.LocalDateTime).now()} }, 'endDate': { $gt: ?#{T(java.time.LocalDateTime).now()} } }")
    List<Promotion> findAllByPromotionType(PromotionType promotionType);
}
