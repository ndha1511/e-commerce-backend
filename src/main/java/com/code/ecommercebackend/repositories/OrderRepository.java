package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
