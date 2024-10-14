package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Promotion;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.Optional;

public interface PromotionRepository extends MongoRepository<Promotion, String> {
    @Query("{'startDate': { $lt: ?#{T(java.time.LocalDateTime).now()} }, 'endDate': { $gt: ?#{T(java.time.LocalDateTime).now()} }, 'applyFor': { $in: [?0] } }")
    Optional<Promotion> findFirstByCurrentDateAndProductId(String productId, Sort sort);

}
