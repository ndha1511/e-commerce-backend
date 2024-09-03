package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
