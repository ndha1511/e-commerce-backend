package com.code.ecommercebackend.services.promotion;

import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.services.BaseService;

import java.util.List;

public interface PromotionService extends BaseService<Promotion, String> {
    List<ProductResponse> getProductsByPromotionId(String promotionId) throws DataNotFoundException;
    List<Promotion> getPromotionsCarousel();
    Promotion getPromotionByUrl(String url) throws DataNotFoundException;
}
