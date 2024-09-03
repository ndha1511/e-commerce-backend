package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.ProductAttribute;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductAttributeRepository extends MongoRepository<ProductAttribute, String> {
}
