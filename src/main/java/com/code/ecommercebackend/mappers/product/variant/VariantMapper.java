package com.code.ecommercebackend.mappers.product.variant;

import com.code.ecommercebackend.dtos.request.product.VariantDto;
import com.code.ecommercebackend.models.Variant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    Variant toVariant(VariantDto variantDto);
}
