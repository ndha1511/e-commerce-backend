package com.code.ecommercebackend.dtos.request.inventory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InventoryDto {
    private String productId;
    private String variantId;
    private int importQuantity;
    private double importPrice;


}
