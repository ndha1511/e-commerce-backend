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
    @Field(name = "variant_id")
    private String variantId;
    @Field(name = "product_name")
    private String productName;
    @Field(name = "inventories")
    private List<InventoryOrder> inventoryOrders;
    private List<String> attributes;
    @Field(name = "product_num_id")
    private long productNumId;
    private double price;
    private int quantity;
    private double amount;
    private String image;
    private String url;
    @Field(name = "allow_comment")
    private boolean allowComment;
    private boolean commented;


}
