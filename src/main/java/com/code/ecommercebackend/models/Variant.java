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
    @Field(name = "product_id")
    private Product product;
    @Field(name = "attribute_value_1")
    private String attributeValue1;
    @Field(name = "attribute_value_2")
    private String attributeValue2;
    private double price;
    private int quantity;
    @Field(name = "discount_id")
    private Discount discount;

}
