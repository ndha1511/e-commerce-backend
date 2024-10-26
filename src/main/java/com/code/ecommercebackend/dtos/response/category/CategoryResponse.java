package com.code.ecommercebackend.dtos.response.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private String categoryName;
    private String image;
    private List<CategoryResponse> children;
}
