package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BrandRepository extends MongoRepository<Brand, String> {
    boolean existsByBrandName(String brandName);
}
