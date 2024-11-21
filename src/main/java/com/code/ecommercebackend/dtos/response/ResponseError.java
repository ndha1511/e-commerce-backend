package com.code.ecommercebackend.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ResponseError implements Response {
    private int status;
    private List<String> errors;


}
