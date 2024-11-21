package com.code.ecommercebackend.mappers.brand;

import com.code.ecommercebackend.dtos.request.brand.CreateBrandRequest;
import com.code.ecommercebackend.exceptions.DataExistsException;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.UploadHelper;
import com.code.ecommercebackend.mappers.product.ProductMapperHelper;
import com.code.ecommercebackend.models.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = {BrandMapperHelper.class,
        UploadHelper.class, ProductMapperHelper.class})
public interface BrandMapper {
    @Mapping(source = "image", target = "image", qualifiedByName = "uploadImage")
    @Mapping(source = "brandName", target = "brandName", qualifiedByName = "checkBrandName")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "checkExistCategory")
    Brand toBrand(CreateBrandRequest createBrandRequest)
            throws FileTooLargeException, FileNotSupportedException, IOException, DataExistsException, DataNotFoundException;
}
