package com.code.ecommercebackend.dtos.response.comment;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CommentResponse<T> {
    private T data;
    private String type;
}