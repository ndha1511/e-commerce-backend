package com.code.ecommercebackend.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document(collection = "product_features")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductFeature {
    @Id
    private String id;
    @Field(name = "user_id")
    private long userId;
    @Field(name = "product_id")
    private long productId;
    @Field(name = "product_name")
    private String productName;
    private String category;
    private String brand;
    private Double price;
    private Integer rating;
    @Field(name = "count_view")
    private int countView;
}
