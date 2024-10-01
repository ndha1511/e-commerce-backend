package com.code.ecommercebackend.mappers.category;

import com.code.ecommercebackend.dtos.request.category.CreateCategoryRequest;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.UploadHelper;
import com.code.ecommercebackend.models.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = {CategoryMapperHelper.class, UploadHelper.class})
public interface CategoryMapper {
    @Mapping(source = "image", target = "image", qualifiedByName = "uploadImage")
    @Mapping(source = "categoryName", target = "categoryName", qualifiedByName = "checkCategoryName")
    Category toCategory(CreateCategoryRequest createCategoryRequest)
            throws FileTooLargeException, FileNotSupportedException, IOException, DataExistsException;
}
