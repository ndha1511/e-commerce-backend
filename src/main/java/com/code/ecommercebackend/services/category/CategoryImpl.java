package com.code.ecommercebackend.services.category;

import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends BaseServiceImpl<Category, String> implements CategoryService {
    public CategoryImpl(MongoRepository<Category, String> repository) {
        super(repository);
    }
}
