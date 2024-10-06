package com.code.ecommercebackend.dtos.response.cart;

import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.models.Variant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCartResponse {
    private int quantity;
    private int quantityInStock;
    private Variant variant;
    private Promotion promotion;
}
