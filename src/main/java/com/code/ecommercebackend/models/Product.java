package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel {
    @Field(name = "product_name")
    private String productName;
    @Field(name = "regular_price")
    private double regularPrice;
    @Field(name = "store_id")
    private String storeId;
    @DocumentReference
    @Field(name = "discount_id")
    private Discount discount;
    private String city;
    @Field(name = "categories")
    private Set<String> categories;
    @Field(name = "brand_id")
    private String brandId;
    @Field(name = "total_quantity")
    private int totalQuantity;
    private String thumbnail;
    @Field(name = "number_of_rating")
    private int numberOfRating;
    @Field(name = "buy_quantity")
    private int buyQuantity;
    private float rating;
}
