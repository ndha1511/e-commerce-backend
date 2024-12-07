package com.code.ecommercebackend.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;


@Document(collection = "product_features")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFeature {
    @Id
    private String id;
    @Field(name = "user_id")
    private Long userId;
    @Field(name = "product_id")
    private Long productId;
    @Field(name = "product_name")
    private String productName;
    private String category;
    private String brand;
    private Double price;
    private Float rating;
    @Field(name = "count_view")
    private Integer countView;
    @Field(name = "view_date")
    private LocalDateTime viewDate;
}
