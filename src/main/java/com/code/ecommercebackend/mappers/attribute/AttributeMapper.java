package com.code.ecommercebackend.mappers.attribute;

import com.code.ecommercebackend.dtos.request.attribute.AttributeDto;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.ProductAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = AttributeMapperHelper.class)
public interface AttributeMapper {
    @Mapping(source = "attributeValues", target = "attributeValues", qualifiedByName = "mapAttributeValues")
    ProductAttribute toProductAttribute(AttributeDto attribute)
            throws FileTooLargeException, FileNotSupportedException, IOException;
}
