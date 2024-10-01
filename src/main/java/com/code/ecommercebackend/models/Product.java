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
    private boolean visible;

    public void createUrlPath() {
        this.urlPath = URLEncoder.encode(this.productName.toLowerCase().trim(), StandardCharsets.UTF_8);
    }
}
