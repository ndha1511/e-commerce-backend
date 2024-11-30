package com.code.ecommercebackend.dtos.response.product;

import com.code.ecommercebackend.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSelling {
    private Product product;
    private Long quantity;
}
