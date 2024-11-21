package com.code.ecommercebackend.mappers.attribute;

import com.code.ecommercebackend.dtos.request.attribute.AttributeValueDto;
import com.code.ecommercebackend.exceptions.FileNotSupportedException;
import com.code.ecommercebackend.exceptions.FileTooLargeException;
import com.code.ecommercebackend.models.AttributeValue;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AttributeMapperHelper {
    private final AttributeValueMapper attributeValueMapper;

    @Named("mapAttributeValues")
    public List<AttributeValue> mapAttributeValues(final List<AttributeValueDto> attributes)
            throws FileTooLargeException, FileNotSupportedException, IOException {
        List<AttributeValue> attributeValues = new ArrayList<>();
        for (AttributeValueDto attribute : attributes) {
            attributeValues.add(attributeValueMapper.toAttributeValue(attribute));
        }
        return attributeValues;
    }
}
