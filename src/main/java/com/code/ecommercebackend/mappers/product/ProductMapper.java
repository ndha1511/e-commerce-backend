package com.code.ecommercebackend.mappers.product;

import com.code.ecommercebackend.dtos.request.product.CreateProductRequest;
import com.code.ecommercebackend.dtos.request.product.ProductExcel;
import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.mappers.UploadHelper;
import com.code.ecommercebackend.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapperHelper.class, UploadHelper.class})
public interface ProductMapper {
    @Mapping(source = "categories", target = "categories", qualifiedByName = "checkExistCategory")
    @Mapping(source = "images", target = "images", qualifiedByName = "uploadImages")
    @Mapping(source = "video", target = "video", qualifiedByName = "uploadVideo")
    Product toProduct(CreateProductRequest createProductRequest) throws DataNotFoundException;
    ProductResponse toProductResponse(Product product);
    Product toProductFromProductExcel(ProductExcel productExcel);
}
