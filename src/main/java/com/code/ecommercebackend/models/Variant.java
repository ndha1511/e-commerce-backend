package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Variant extends BaseModel {
    private String product;
    @Field(name = "attribute_value_1")
    private String attributeValue1;
    @Field(name = "attribute_value_2")
    private String attributeValue2;
    private double price;
    private String sku;
}
