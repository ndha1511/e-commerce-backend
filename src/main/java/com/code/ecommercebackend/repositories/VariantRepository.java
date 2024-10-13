package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Variant;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface VariantRepository extends MongoRepository<Variant, String> {
    Optional<Variant> findByProductIdAndAttributeValue1AndAttributeValue2(String product, String attr1, String attr2);
    Optional<Variant> findByProductIdAndAttributeValue1(String product, String attr1);
}
