package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Document(collection = "shops")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shop extends BaseModel {
    @Field(name = "store_name")
    private String storeName;
    private String owner;
    @Indexed(unique = true)
    @Field(name = "url_path")
    private String urlPath;
    @Field(name = "number_of_product")
    private int numberOfProduct;
    @Field(name = "number_of_following")
    private int numberOfFollowing;
    @Field(name = "number_of_rating")
    private int numberOfRating;
    private String description;
    private float rating;
    private String logo;
    private Address address;


    public void createUrlPath() {
        this.urlPath = URLEncoder.encode(this.storeName.toLowerCase().trim(), StandardCharsets.UTF_8);
    }
}
