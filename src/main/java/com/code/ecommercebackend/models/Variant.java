package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndex(
        def = "{'product': 1, 'attributeValue1': 1, 'attributeValue2': 1}",
        unique = true
)
public class Variant {
    @Id
    private String id;
    @DocumentReference
    private Product product;
    @Field(name = "attribute_value_1")
    private String attributeValue1;
    @Field(name = "attribute_value_2")
    private String attributeValue2;
    private double price;
    private String sku;
}
