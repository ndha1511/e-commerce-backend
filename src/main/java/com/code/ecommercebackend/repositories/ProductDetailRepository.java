package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.ProductDetail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductDetailRepository extends MongoRepository<ProductDetail, String> {

}
