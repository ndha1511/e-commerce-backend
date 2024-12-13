package com.code.ecommercebackend.dtos.response.purchaseOrder;

import com.code.ecommercebackend.models.Variant;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PurchaseItem {
    private Variant variant;
    private int quantity;
    private double importPrice;
}
