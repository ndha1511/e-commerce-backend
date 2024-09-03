package com.code.ecommercebackend.mappers.category;

import com.code.ecommercebackend.dtos.request.category.CreateCategoryRequest;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = CategoryMapperHelper.class)
public interface CategoryMapper {
    @Mapping(source = "image", target = "image", qualifiedByName = "uploadCategoryImage")
    Category toCategory(CreateCategoryRequest createCategoryRequest) throws FileTooLargeException, FileNotSupportedException, IOException;
}
