package com.code.ecommercebackend.mappers.attribute;

import com.code.ecommercebackend.dtos.request.attribute.VariantDto;
import com.code.ecommercebackend.dtos.response.variant.VariantResponse;
import com.code.ecommercebackend.models.Variant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VariantMapper {
    Variant toVariant(VariantDto variantDto);
    VariantResponse toVariantResponse(Variant variant);
}
