package com.code.ecommercebackend.services.recommend;

import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.dtos.response.recommend.ProductRecommend;

import java.util.List;

public interface RecommendService {
    List<ProductRecommend> getProductsRecommended(long userId, int nRecommend);
}
