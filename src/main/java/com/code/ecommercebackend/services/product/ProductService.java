package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.services.BaseService;


public interface ProductService extends BaseService<Product, String> {
    PageResponse<ProductResponse> getPageProduct(int pageNo, int size, String[] search, String[] sort);
}
