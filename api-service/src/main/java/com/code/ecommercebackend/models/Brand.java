package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "brands")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Brand extends BaseModel {
    @Field(name = "brand_name")
    @Indexed(unique = true)
    private String brandName;
    private String description;
    private String image;
    private Set<String> categories;
    @Field(name = "url_path")
    @Indexed(unique = true)
    private String urlPath;

    public void createUrlPath() {
        this.urlPath = this.brandName.toLowerCase().trim().replaceAll("[ |&,.\\\\]+", "-");
    }
}
