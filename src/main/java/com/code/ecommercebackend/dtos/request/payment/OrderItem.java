package com.code.ecommercebackend.dtos.request.payment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItem {
    private String variantId;
    private int quantity;
}
