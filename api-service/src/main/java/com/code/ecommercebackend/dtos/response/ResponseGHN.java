package com.code.ecommercebackend.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGHN<T> {
    private int code;
    private String message;
    private T data;
}
