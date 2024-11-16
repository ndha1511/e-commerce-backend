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
    private Integer rating;
    @Field(name = "count_view")
    private int countView;
}
