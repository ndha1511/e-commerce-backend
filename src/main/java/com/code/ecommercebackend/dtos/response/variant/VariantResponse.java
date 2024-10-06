package com.code.ecommercebackend.dtos.response.variant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantResponse {
    private String id;
    private String productId;
    private String attributeValue1;
    private String attributeValue2;
    private double price;
    private double discountPrice;
    private float discountPercent;
    private String sku;
}
