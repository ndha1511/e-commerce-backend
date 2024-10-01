package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepository extends MongoRepository<Inventory, String> {
}
