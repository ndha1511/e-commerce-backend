package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByUrlPath(String urlPath);
}
