package com.code.ecommercebackend.dtos.response.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse<T> {
    private T data;
    private String type;
}
