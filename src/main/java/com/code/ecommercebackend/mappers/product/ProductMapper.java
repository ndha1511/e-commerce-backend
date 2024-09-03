package com.code.ecommercebackend.mappers.product;

import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(CreateProductRequest createProductRequest);
}
