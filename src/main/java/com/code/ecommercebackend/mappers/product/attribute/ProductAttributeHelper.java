package com.code.ecommercebackend.mappers.product.attribute;

import com.code.ecommercebackend.dtos.request.product.AttributeValueDto;
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
public class ProductAttributeHelper {
    private final AttributeValueMapper attributeValueMapper;

    @Named("mapAttributeValue")
    public List<AttributeValue> mapAttributeValue(List<AttributeValueDto> attributeValuesDto) throws FileTooLargeException, FileNotSupportedException, IOException {
        List<AttributeValue> list = new ArrayList<>();
        for (AttributeValueDto attributeValueDto : attributeValuesDto) {
            AttributeValue attributeValue = attributeValueMapper.toAttributeValue(attributeValueDto);
            list.add(attributeValue);
        }
        return list;
    }
}
