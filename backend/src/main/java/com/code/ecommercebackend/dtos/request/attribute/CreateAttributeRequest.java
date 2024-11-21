package com.code.ecommercebackend.dtos.request.attribute;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateAttributeRequest {
    private String productId;
    private List<AttributeDto> attributes;
    private List<VariantDto> variants;
}
