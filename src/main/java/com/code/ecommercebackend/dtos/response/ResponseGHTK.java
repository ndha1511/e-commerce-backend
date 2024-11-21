package com.code.ecommercebackend.dtos.response;

import com.code.ecommercebackend.dtos.response.payment.Fee;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseGHTK {
    private boolean success;
    private String message;
    private Fee fee;
}
