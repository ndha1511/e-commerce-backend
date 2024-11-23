package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.InventoryDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventoryRepository extends MongoRepository<InventoryDetail, String> {
    List<InventoryDetail> findByProductId(String productId);
    List<InventoryDetail> findByVariantId(String variantId);
    List<InventoryDetail> findByProductIdAndVariantIdOrderByImportDate(String productId, String variantId);
}
