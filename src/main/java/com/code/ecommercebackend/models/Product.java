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
        this.urlPath = this.productName.toLowerCase().trim().replaceAll("[ ,.\\\\]+", "-");;
    }

    public void normalizerName() {
        List<String> namesNormalize = new ArrayList<>();
        String normalize = this.productName.replaceAll("[^a-zA-Z0-9\\s]", "").trim();
        namesNormalize.add(normalize);
        String normalizedText = Normalizer.normalize(normalize, Normalizer.Form.NFD);
        String noAccentText = normalizedText.replaceAll("\\p{M}", "");
        namesNormalize.add(noAccentText);
        this.searchNames = namesNormalize;

    }
}
