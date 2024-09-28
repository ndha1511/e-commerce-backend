package com.code.ecommercebackend.services.category;

import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.repositories.CategoryRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends BaseServiceImpl<Category, String> implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryImpl(MongoRepository<Category, String> repository,
                        CategoryRepository categoryRepository) {
        super(repository);
        this.categoryRepository = categoryRepository;

    }

    @Override
    public Category save(Category category) {
        if(category.getParentId() != null) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElse(null);
            if(parent != null) {
                parent.setChildren(parent.getChildren() + 1);
                categoryRepository.save(parent);
                category.setLevel(parent.getLevel() + 1);
            }
        }

        return super.save(category);
    }
}
