package com.code.ecommercebackend.dtos.request.product;

import com.code.ecommercebackend.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VariantExcel {
    private Product product;
    private String attributeValue1;
    private String attributeValue2;
    private double price;
    private String sku;
    private int quantity;
    private double importPrice;
}
