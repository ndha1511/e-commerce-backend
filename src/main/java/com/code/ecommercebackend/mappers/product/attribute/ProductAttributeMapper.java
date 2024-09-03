package com.code.ecommercebackend.mappers.product.attribute;

import com.code.ecommercebackend.dtos.request.product.ProductAttributeDto;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = ProductAttributeHelper.class)
public interface ProductAttributeMapper {
    @Mapping(source = "attributeValues", target = "attributeValues", qualifiedByName = "mapAttributeValue")
    ProductAttribute toProductAttribute(ProductAttributeDto productAttributeDto)  throws FileTooLargeException, FileNotSupportedException, IOException;
}
