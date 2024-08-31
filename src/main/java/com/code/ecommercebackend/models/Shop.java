package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "shops")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shop extends BaseModel {
    @Field(name = "store_name")
    private String storeName;
    private String owner;
    private int numberOfProduct;
    private int numberOfFollowing;
    private int numberOfRating;
    private float rating;
    private String logo;
    private Address address;
}
