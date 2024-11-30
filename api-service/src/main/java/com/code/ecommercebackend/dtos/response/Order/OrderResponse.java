package com.code.ecommercebackend.dtos.response.Order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse<T> {
    private T data;
    private String type;
}
