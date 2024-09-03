package com.code.ecommercebackend.dtos.request.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductAttributeDto {
    private String attributeName;
    private List<AttributeValueDto> attributeValues;
}
