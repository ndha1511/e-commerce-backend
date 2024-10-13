package com.code.ecommercebackend.dtos.response.cart;

import com.code.ecommercebackend.dtos.response.variant.VariantResponse;
import com.code.ecommercebackend.models.Promotion;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductCartResponse {
    private int quantity;
    private VariantResponse variantResponse;
    private Promotion promotion;
}
