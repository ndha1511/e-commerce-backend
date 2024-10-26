package com.code.ecommercebackend.repositories;

import com.code.ecommercebackend.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends MongoRepository<Category, String> {
    boolean existsByCategoryName(String categoryName);
    List<Category> findByParentId(String parentId);
    Optional<Category> findByCategoryName(String categoryName);
}
