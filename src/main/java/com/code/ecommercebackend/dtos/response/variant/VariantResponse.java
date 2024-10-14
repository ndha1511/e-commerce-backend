package com.code.ecommercebackend.dtos.response.variant;

import com.code.ecommercebackend.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantResponse {
    private String id;
    private Product product;
    private String image;
    private String attributeValue1;
    private String attributeValue2;
    private double price;
    private String sku;
    private int quantity;
    private int buyQuantity;
}
