package com.code.ecommercebackend.dtos.response.category;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {
    private String id;
    private String categoryName;
    private String image;
    private String urlPath;
    private List<CategoryResponse> children;
}
