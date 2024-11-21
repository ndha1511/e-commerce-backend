package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.PurchaseOrder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PurchaseOrderRepository extends MongoRepository<PurchaseOrder, String> {
}
