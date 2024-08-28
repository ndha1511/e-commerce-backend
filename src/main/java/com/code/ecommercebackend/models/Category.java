package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseModel {
    @Field(name = "category_name")
    private String categoryName;
    private String image;
    @DocumentReference
    @Field(name = "parent_id")
    private Category parentCategory;
}
