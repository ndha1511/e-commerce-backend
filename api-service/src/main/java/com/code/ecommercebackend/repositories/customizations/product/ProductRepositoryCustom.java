package com.code.ecommercebackend.repositories.customizations.product;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.models.Product;

public interface ProductRepositoryCustom {
    PageResponse<Product> getPageData(int pageNo, int size, String[] search, String[] sort,
                                      String rangeRegularPrice,
                                      String rangeRating);
}
