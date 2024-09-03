package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Set;

@Document(collection = "product_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetail extends BaseModel {
    @Field(name = "product_id")
    private String productId;
    private String description;
    private Set<Tag> tags;
    private List<ProductImage> images;
}
