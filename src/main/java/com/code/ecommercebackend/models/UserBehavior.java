package com.code.ecommercebackend.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "user_behavior")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBehavior {
    @Id
    private String id;
    @Field(name = "user_id")
    private long userId;
    @Field(name = "product_id")
    private long productId;
    /**
     * @value 1 = view
     * @value 2 = buy
     * @value 3 = comment
     */
    private int behavior;
    @Field(name = "buy_quantity")
    private Integer buyQuantity;
    private Integer rating;
}
