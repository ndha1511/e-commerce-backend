package com.code.ecommercebackend.dtos.response.attribute;

import com.code.ecommercebackend.models.ProductAttribute;
import com.code.ecommercebackend.models.Variant;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AttributeResponse {
    List<ProductAttribute> attributes;
    List<Variant> variants;
}
