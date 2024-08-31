package com.code.ecommercebackend.mappers.category;

import com.code.ecommercebackend.dtos.request.category.CreateCategoryRequest;
import com.code.ecommercebackend.models.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CreateCategoryRequest createCategoryRequest);
}
