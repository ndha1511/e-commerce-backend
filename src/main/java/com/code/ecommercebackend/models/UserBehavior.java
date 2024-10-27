package com.code.ecommercebackend.models;


import com.code.ecommercebackend.models.enums.Behavior;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Document(collection = "user_behavior")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBehavior {
    @Id
    private String id;
    @Field(name = "user_id")
    private String userId;
    @Field(name = "product_id")
    private String productId;
    private Behavior behavior;
    private LocalDateTime time;
    @Field(name = "buy_quantity")
    private Integer buyQuantity;
    private Integer rating;
}
