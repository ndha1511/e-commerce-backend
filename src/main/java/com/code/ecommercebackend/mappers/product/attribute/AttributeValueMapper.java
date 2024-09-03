package com.code.ecommercebackend.mappers.product.attribute;

import com.code.ecommercebackend.dtos.request.product.AttributeValueDto;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = AttributeValueHelper.class)
public interface AttributeValueMapper {
    @Mapping(source = "image", target = "image", qualifiedByName = "attributeValueUpload")
    AttributeValue toAttributeValue(AttributeValueDto attributeValueDto) throws FileTooLargeException, FileNotSupportedException, IOException;
}
