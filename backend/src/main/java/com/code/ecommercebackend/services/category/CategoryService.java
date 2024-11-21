package com.code.ecommercebackend.services.category;

import com.code.ecommercebackend.dtos.response.category.CategoryResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.services.BaseService;

import java.util.List;

public interface CategoryService extends BaseService<Category, String> {
    CategoryResponse findCategoryByRoot(String rootId) throws DataNotFoundException;
    CategoryResponse findCategoryByUrlRoot(String url) throws DataNotFoundException;
    Category findByUrl(String url) throws DataNotFoundException;
    List<Category> findAllCategories(List<String> categoryId);
}
