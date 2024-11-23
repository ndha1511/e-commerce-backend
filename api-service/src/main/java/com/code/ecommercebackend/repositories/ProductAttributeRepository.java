package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.ProductAttribute;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProductAttributeRepository extends MongoRepository<ProductAttribute, String> {
    List<ProductAttribute> findByProductId(String productId);
}
