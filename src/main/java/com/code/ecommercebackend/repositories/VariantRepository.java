package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VariantRepository extends MongoRepository<Variant, String> {
}
