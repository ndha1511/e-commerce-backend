package com.code.ecommercebackend.mappers.brand;

import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.repositories.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BrandMapperHelper {
    private final BrandRepository brandRepository;

    @Named("checkBrandName")
    public String checkBrandName(String brandName) throws DataExistsException {
        if(brandRepository.existsByBrandName(brandName))
            throw new DataExistsException("brand name is exists");
        return brandName;
    }
}
