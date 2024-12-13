package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Promotion;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends MongoRepository<Promotion, String> {
    @Query("{ 'startDate': { $lt: ?#{T(com.code.ecommercebackend.components.LocalDateTimeVN).now()} }, 'endDate': { $gt: ?#{T(com.code.ecommercebackend.components.LocalDateTimeVN).now()} }, $or: [ { 'applyAll': true }, { 'applyFor': { $in: [?0] } }]} ")
    List<Promotion> findFirstByCurrentDateAndProductId(String productId, Sort sort);
    @Query("{ 'startDate': { $lt: ?#{T(com.code.ecommercebackend.components.LocalDateTimeVN).now()} }, 'endDate': { $gt: ?#{T(com.code.ecommercebackend.components.LocalDateTimeVN).now()} }, 'view': true }")
    List<Promotion> findAllByCurrentDateAndView();
    @Query("{ 'startDate': { $lt: ?#{T(com.code.ecommercebackend.components.LocalDateTimeVN).now()} }, 'endDate': { $gt: ?#{T(com.code.ecommercebackend.components.LocalDateTimeVN).now()} }, 'flash_sale': true }")
    List<Promotion> findAllByCurrentDateAndFlashSale();
    Optional<Promotion> getPromotionByUrl(String url);

}
