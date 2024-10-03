package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.List;
import java.util.Set;

@Document(collection = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel {
    @Field(name = "product_name")
    private String productName;
    @Indexed(unique = true)
    @Field(name = "url_path")
    private String urlPath;
    @Field(name = "categories")
    private Set<String> categories;
    @Field(name = "brand_id")
    private String brandId;
    private String thumbnail;
    private String description;
    private Set<Tag> tags;
    private List<String> images;
    private String video;
    private int likes;
    @Field(name = "regular_price")
    private double regularPrice;
    @Field(name = "total_quantity")
    private int totalQuantity;
    private int reviews;
    private float rating;


    public void createUrlPath() {
        this.urlPath = this.productName.toLowerCase().trim().replace(" ", "-");
    }
}
