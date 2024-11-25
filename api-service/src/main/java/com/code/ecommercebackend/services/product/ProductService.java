package com.code.ecommercebackend.services.product;

import com.code.ecommercebackend.dtos.response.PageResponse;
import com.code.ecommercebackend.dtos.response.attribute.AttributeResponse;
import com.code.ecommercebackend.dtos.response.product.ProductResponse;
import com.code.ecommercebackend.exceptions.DataNotFoundException;
import com.code.ecommercebackend.models.Product;
import com.code.ecommercebackend.services.BaseService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


public interface ProductService extends BaseService<Product, String> {
    PageResponse<ProductResponse> getPageProduct(int pageNo, int size, String[] search, String[] sort);
    ProductResponse findByUrl(String url, HttpServletRequest request) throws DataNotFoundException;
    AttributeResponse findAttributeByProductId(String productId);
    List<ProductResponse> getProductResponseByNumIds(List<Long> ids);
    List<ProductResponse> getProductResponseById(Iterable<String> ids);

}
