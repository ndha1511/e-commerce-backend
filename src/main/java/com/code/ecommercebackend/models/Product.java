package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Document(collection = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseModel {
    @Indexed(unique = true)
    @Field(name = "num_id")
    private Long numId;
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
    private List<Tag> tags;
    private List<String> images;
    private String video;
    @Field(name = "regular_price")
    private double regularPrice;
    @Field(name = "total_quantity")
    private int totalQuantity;
    @Field(name = "buy_quantity")
    private int buyQuantity;
    @Field(name = "search_names")
    private List<String> searchNames;
    private int reviews;
    private int weight;
    private float rating;


    public void createUrlPath() {
        this.urlPath = this.productName.toLowerCase().trim().replaceAll("[ |&,/.\\\\]+", "-");;
    }

    public void normalizerName() {
        List<String> namesNormalize = new ArrayList<>();
        namesNormalize.add(this.productName.toLowerCase());
        String temp = Normalizer.normalize(this.productName, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String normalize = pattern.matcher(temp).replaceAll("").toLowerCase();
        namesNormalize.add(normalize);
        this.searchNames = namesNormalize;

    }
}
