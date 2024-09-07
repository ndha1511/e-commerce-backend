package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByCategoryName(String categoryName);
}
