package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findByProductId(String productId);
    List<Inventory> findByVariantId(String variantId);
    List<Inventory> findByProductIdAndVariantIdOrderByImportDate(String productId, String variantId);
}
