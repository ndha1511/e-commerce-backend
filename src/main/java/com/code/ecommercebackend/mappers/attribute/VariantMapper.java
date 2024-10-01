package com.code.ecommercebackend.mappers.attribute;

import com.code.ecommercebackend.dtos.request.attribute.VariantDto;
import com.code.ecommercebackend.models.Variant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    Variant toVariant(VariantDto variantDto);
}
