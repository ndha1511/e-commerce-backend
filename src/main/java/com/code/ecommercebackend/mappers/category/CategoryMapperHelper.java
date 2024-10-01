package com.code.ecommercebackend.mappers.category;

import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.repositories.CategoryRepository;
import com.code.ecommercebackend.utils.S3Upload;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class CategoryMapperHelper {
    private final CategoryRepository categoryRepository;


    @Named("checkCategoryName")
    public String checkCategoryName(String categoryName) throws DataExistsException {
        if(categoryRepository.existsByCategoryName(categoryName))
            throw new DataExistsException("category name is exists");
        return categoryName;
    }
}
