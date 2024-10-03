package com.code.ecommercebackend.models;

import com.code.ecommercebackend.models.enums.ConditionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Condition {
    @Field(name = "condition_type")
    private ConditionType conditionType;
    @Field(name = "min_price")
    private Double minPrice;
    @Field(name = "buy_quantity")
    private Integer buyQuantity;

}
