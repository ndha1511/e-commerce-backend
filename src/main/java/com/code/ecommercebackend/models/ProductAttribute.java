package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Set;

@Document(collection = "product_attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttribute extends BaseModel {
    @Field(name = "product_id")
    private String productId;
    @Field(name = "attribute_name")
    private String attributeName;
    @Field(name = "attribute_values")
    private Set<AttributeValue> attributeValues;
}
