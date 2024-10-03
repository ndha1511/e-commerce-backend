package com.code.ecommercebackend.mappers.promotion;

import com.code.ecommercebackend.dtos.request.promotion.CreatePromotionRequest;
import com.code.ecommercebackend.models.Promotion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    Promotion toPromotion(CreatePromotionRequest createPromotionRequest);
}
