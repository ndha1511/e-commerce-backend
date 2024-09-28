package com.code.ecommercebackend.mappers.product;

import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = ProductMapperHelper.class)
public interface ProductMapper {
    @Mapping(source = "categories", target = "categories", qualifiedByName = "checkExistCategory")
    Product toProduct(CreateProductRequest createProductRequest) throws DataNotFoundException;
}
