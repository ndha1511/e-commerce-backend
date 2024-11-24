package com.code.ecommercebackend.repositories.customizations.productFeature;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCount {
    @Id
    @Field("_id")
    private String id;
    private int count;

}
