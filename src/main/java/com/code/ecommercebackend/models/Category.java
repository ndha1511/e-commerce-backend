package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@CompoundIndex(
        def = "{'categoryName': 1, 'parentId': 1}",
        unique = true
)
public class Category extends BaseModel {
    @Field(name = "category_name")
    private String categoryName;
    private String image;
    @Field(name = "parent_id")
    private String parentId;
    private int children;
    private int level;
}
