package com.code.ecommercebackend.mappers.attribute;

import com.code.ecommercebackend.dtos.request.attribute.AttributeValueDto;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.mappers.UploadHelper;
import com.code.ecommercebackend.models.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.io.IOException;

@Mapper(componentModel = "spring", uses = UploadHelper.class)
public interface AttributeValueMapper {
    @Mapping(source = "image", target = "image", qualifiedByName = "uploadImage")
    AttributeValue toAttributeValue(AttributeValueDto attributeValueDto)
            throws FileTooLargeException, FileNotSupportedException, IOException;
}
