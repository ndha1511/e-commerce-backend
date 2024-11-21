package com.code.ecommercebackend.dtos.response.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse<T> {
    private T data;
    private String type;
}