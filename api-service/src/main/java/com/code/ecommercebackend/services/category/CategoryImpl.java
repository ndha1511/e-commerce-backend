package com.code.ecommercebackend.services.category;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.dtos.response.category.CategoryResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Category;
import com.code.ecommercebackend.repositories.CategoryRepository;
import com.code.ecommercebackend.repositories.customizations.redis.RedisRepository;
import com.code.ecommercebackend.services.BaseServiceImpl;
import com.code.ecommercebackend.utils.RedisKeyHandler;
import com.code.ecommercebackend.utils.enums.RedisKeyEnum;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryImpl extends BaseServiceImpl<Category, String> implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final RedisRepository redisRepository;

    public CategoryImpl(MongoRepository<Category, String> repository,
                        CategoryRepository categoryRepository, RedisRepository redisRepository) {
        super(repository);
        this.categoryRepository = categoryRepository;

        this.redisRepository = redisRepository;
    }

    @Override
    public Category save(Category category) {
        long count = categoryRepository.count();
        count +=2;
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

    @Override
    public List<Category> findAllCategories(List<String> categoryId) {
        return categoryRepository.findAllById(categoryId);
    }

    @Override
    public PageResponse<Category> getPageData(int pageNo, int size, String[] search, String[] sort, Class<Category> clazz) {
        String key = RedisKeyHandler.createKeyWithPageQuery(pageNo, size, search, sort, RedisKeyEnum.CATEGORY);
        try {
            PageResponse<Category> result = redisRepository.getPageDataInCache(key, clazz);
            if(result == null) {
                result = super.getPageData(pageNo, size, search, sort, clazz);
                redisRepository.saveDataInCache(key, result);
            }
            return result;
        } catch (JsonProcessingException e) {
            return null;
        }

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
