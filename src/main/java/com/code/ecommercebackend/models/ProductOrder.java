package com.code.ecommercebackend.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class  ProductOrder {
    @Field(name = "product_id")
    private String productId;
    @Field(name = "product_name")
    private String productName;
    @Field(name = "inventories")
    private List<String> inventories;
    private List<String> attributes;
    private double price;
    private int quantity;
    private double amount;
    private String image;
    @Field(name = "allow_comment")
    private boolean allowComment;
    private boolean commented;


}
