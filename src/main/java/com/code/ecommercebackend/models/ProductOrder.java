package com.code.ecommercebackend.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;


@Getter
@Setter
@Builder
public class ProductOrder {
    @Field(name = "product_id")
    private String productId;
    @Field(name = "product_name")
    private String productName;
    private Set<String> attributes;
    private double price;
    private int quantity;
    private double amount;
    private String image;
    @Field(name = "allow_comment")
    private boolean allowComment;
    private boolean commented;


}
