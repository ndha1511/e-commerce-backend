package com.code.ecommercebackend.services.category;

import com.code.ecommercebackend.dtos.response.category.CategoryResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.repositories.CategoryRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        long count = categoryRepository.count();
        count++;
        category.setNumId(count);
        if(category.getParentId() != null) {
            Category parent = categoryRepository.findById(category.getParentId())
                    .orElse(null);
            if(parent != null) {
                parent.setChildren(parent.getChildren() + 1);
                categoryRepository.save(parent);
                category.setLevel(parent.getLevel() + 1);
            }
        }
        category.createUrlPath();

        return super.save(category);
    }

    @Override
    public CategoryResponse findCategoryByRoot(String rootId) throws DataNotFoundException {
        CategoryResponse categoryResponse = new CategoryResponse();
        Category category = categoryRepository.findById(rootId)
                .orElseThrow(() -> new DataNotFoundException("category not found"));
        categoryResponse.setCategoryName(category.getCategoryName());
        categoryResponse.setImage(category.getImage());
        categoryResponse.setUrlPath(category.getUrlPath());
        categoryResponse.setId(category.getId());
        if(category.getChildren() > 0) {
            categoryResponse.setChildren(getSubCategories(category.getId()));
        }

        return categoryResponse;
    }

    @Override
    public CategoryResponse findCategoryByUrlRoot(String url) throws DataNotFoundException {
        CategoryResponse categoryResponse = new CategoryResponse();
        Category category = categoryRepository.findByUrlPath(url)
                .orElseThrow(() -> new DataNotFoundException("category not found"));
        categoryResponse.setCategoryName(category.getCategoryName());
        categoryResponse.setImage(category.getImage());
        categoryResponse.setUrlPath(category.getUrlPath());
        categoryResponse.setId(category.getId());
        if(category.getChildren() > 0) {
            categoryResponse.setChildren(getSubCategories(category.getId()));
        }

        return categoryResponse;
    }

    @Override
    public Category findByUrl(String url) throws DataNotFoundException {
        return categoryRepository.findByUrlPath(url)
                .orElseThrow(() -> new DataNotFoundException("category not found"));
    }

    private List<CategoryResponse> getSubCategories(String parentId) {
        List<Category> subCategories = categoryRepository.findByParentId(parentId);
        List<CategoryResponse> subCategoriesResponse = new ArrayList<>();
        for(Category subCategory : subCategories) {
            CategoryResponse subCategoryResponse = new CategoryResponse();
            subCategoryResponse.setCategoryName(subCategory.getCategoryName());
            subCategoryResponse.setImage(subCategory.getImage());
            subCategoryResponse.setUrlPath(subCategory.getUrlPath());
            subCategoryResponse.setId(subCategory.getId());
            if(subCategory.getChildren() > 0) {
                subCategoryResponse.setChildren(getSubCategories(subCategory.getId()));
            }
            subCategoriesResponse.add(subCategoryResponse);
        }
        return subCategoriesResponse;
    }
}
