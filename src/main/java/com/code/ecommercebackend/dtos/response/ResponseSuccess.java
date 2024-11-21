package com.code.ecommercebackend.dtos.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSuccess<T> implements Response {
    private int status;
    private String message;
    private T data;

    public ResponseSuccess(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseSuccess(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
