package com.code.ecommercebackend.models;

import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;


@Getter
@Builder
public class ProductOrder {
    @Field(name = "product_id")
    private String productId;
    @Field(name = "product_name")
    private String productName;
    private Set<String> attributes;
    private double price;
    @Field(name = "discount_price")
    private double discountPrice;
    private int quantity;
    private double amount;
    private String image;

    public void calcAmount() {
        this.amount = (this.price - this.discountPrice) * this.quantity;
    }




}
