package com.code.ecommercebackend.mappers.product;

import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductMapperHelper {
    private final CategoryRepository categoryRepository;

    @Named("checkExistCategory")
    public Set<String> checkExistsCategory(Set<String> categories) throws DataNotFoundException {
        for (String category : categories) {
            if (!categoryRepository.existsById(category))
                throw  new DataNotFoundException("category does not exist");
        }
        return categories;
    }
}
