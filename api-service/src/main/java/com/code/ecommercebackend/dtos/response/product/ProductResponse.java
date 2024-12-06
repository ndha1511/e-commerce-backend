package com.code.ecommercebackend.dtos.response.product;

import com.code.ecommercebackend.models.ProductAttribute;
import com.code.ecommercebackend.models.Promotion;
import com.code.ecommercebackend.models.Tag;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private String id;
    private String productName;
    private String urlPath;
    private Set<String> categories;
    private String brandId;
    private String thumbnail;
    private String description;
    private List<Tag> tags;
    private List<String> images;
    private String video;
    private int likes;
    private double regularPrice;
    private int totalQuantity;
    private int buyQuantity;
    private int reviews;
    private float rating;
    private int weight;
    private Long numId;
    private Promotion promotion;
    private List<ProductAttribute> attributes;
}
