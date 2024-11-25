package com.code.ecommercebackend.mappers.promotion;

import com.code.ecommercebackend.dtos.request.promotion.CreatePromotionRequest;
import com.code.ecommercebackend.mappers.UploadHelper;
import com.code.ecommercebackend.models.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UploadHelper.class})
public interface PromotionMapper {
    @Mapping(source = "image", target = "image", qualifiedByName = "uploadImage")
    Promotion toPromotion(CreatePromotionRequest createPromotionRequest);
}
