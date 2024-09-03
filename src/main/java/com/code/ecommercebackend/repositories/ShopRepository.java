package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ShopRepository extends MongoRepository<Shop, String> {
}
