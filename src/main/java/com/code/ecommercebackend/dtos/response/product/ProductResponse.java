package com.code.ecommercebackend.dtos.response.product;

import com.code.ecommercebackend.models.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ProductResponse {
    private String productName;
    private String urlPath;
    private Set<String> categories;
    private String brandId;
    private String thumbnail;
    private String description;
    private Set<Tag> tags;
    private List<String> images;
    private String video;
    private int likes;
    private double regularPrice;
    private int totalQuantity;
    private int buyQuantity;
    private int reviews;
    private float rating;
    private double discountedPrice;
    private float discountPercent;
}
