package com.code.ecommercebackend.dtos.response.category;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CategoryResponse {
    private String id;
    private String categoryName;
    private String image;
    private String urlPath;
    private List<CategoryResponse> children;
}
