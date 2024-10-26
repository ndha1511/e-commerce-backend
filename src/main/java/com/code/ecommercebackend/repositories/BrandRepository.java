package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Brand;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BrandRepository extends MongoRepository<Brand, String> {
    boolean existsByBrandName(String brandName);
    Optional<Brand> findByBrandName(String brandName);
}
