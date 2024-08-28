package com.code.ecommercebackend.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "brands")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Brand extends BaseModel {
    @Field(name = "brand_name")
    private String brandName;
}
