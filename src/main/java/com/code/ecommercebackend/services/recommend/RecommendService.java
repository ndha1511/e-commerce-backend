package com.code.ecommercebackend.services.recommend;

import com.code.ecommercebackend.dtos.response.product.ProductResponse;

import java.util.List;

public interface RecommendService {
    List<ProductResponse> getProductsRecommended(long userId, int nRecommend);
}
