package com.code.ecommercebackend.dtos.request.attribute;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttributeDto {
    private String attributeName;
    private List<AttributeValueDto> attributeValues;
}
