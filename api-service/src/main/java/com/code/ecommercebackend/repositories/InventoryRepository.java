package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.dtos.response.statistics.TopProductSelling;
import com.code.ecommercebackend.models.Inventory;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
    List<Inventory> findByProductId(String productId);
    List<Inventory> findByVariantId(String variantId);
    List<Inventory> findByProductIdAndVariantIdOrderByImportDate(String productId, String variantId);
    @Aggregation(pipeline = {
            "{ $match: { 'import_date': { $gte: ?0 } } }",
            "{ $group: { _id: '$product_id', totalSaleQuantity: { $sum: '$sale_quantity' }, totalImportQuantity: { $sum: '$import_quantity' } } }",
            "{ $match: { 'totalImportQuantity': { $lt: 20 } } }",
            "{ $sort: { totalSaleQuantity: -1 } }",
            "{ $limit: 5 }"
    })
    List<TopProductSelling> getTop5BestSelling(LocalDateTime datetime);

    @Aggregation(pipeline = {
            "{ $match: { 'import_date': { $gte: ?0 } } }",
            "{ $group: { _id: '$product_id', totalSaleQuantity: { $sum: '$sale_quantity' }, totalImportQuantity: { $sum: '$import_quantity' } } }",
            "{ $match: { 'totalImportQuantity': { $lt: 20 } } }",
            "{ $sort: { totalSaleQuantity: 1 } }",
            "{ $limit: 5 }"
    })
    List<TopProductSelling> findBottom5ProductsBySalesAndLowImportQuantity(LocalDateTime datetime);
}
